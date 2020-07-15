package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.*;
import com.ppdai.atlas.dto.query.AppManagerQuery;
import com.ppdai.atlas.dto.query.AppQuery;
import com.ppdai.atlas.manager.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AppService {

    private final AppManager appManager;

    private final AppQuotaManager appQuotaManager;

    private final EnvManager envManager;

    private final SpecTypeManager specTypeManager;

    private final UserManager userManager;


    @Autowired
    public AppService(AppManager appManager, AppQuotaManager appQuotaManager, EnvManager envManager,
                      SpecTypeManager specTypeManager, UserManager userManager
) {
        this.appManager = appManager;
        this.appQuotaManager = appQuotaManager;
        this.envManager = envManager;
        this.specTypeManager = specTypeManager;
        this.userManager = userManager;
    }

    public Optional<AppDto> getAppByAppId(String id) {
        Preconditions.checkNotNull(id, "id must not be null. ");
        return appManager.getAppByAppId(id);
    }

    public Optional<AppDtoPlus> getAppPlusByAppId(String id) {
        Preconditions.checkNotNull(id, "id must not be null. ");
        return appManager.getAppPlusByAppId(id);
    }

    @Transactional
    public AppDtoPlus addApp(AppDtoPlus app) {
        Preconditions.checkNotNull(app, "query must not be null.");
        AppDtoPlus appDto = appManager.addApp(app);
        appQuotaManager.initAppQuotas(app.getAppId());
        return appDto;
    }

    public List<AppDto> getAllApps(Long orgId) {
        return appManager.getAllApps(orgId);
    }

    public List<AppDtoPlus> getAllAppPlus(Long orgId) {
        return appManager.getAllAppPlus(orgId);
    }

    public PageDto<AppDto> getAppsByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return appManager.getAppByPage(page, size);
    }

    public PageDto<AppDtoPlus> getAppPlusByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return appManager.getAppPlusByPage(page, size);
    }

    public void removeAppById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = appManager.getAppByAutoIncreId(id).isPresent();
        Preconditions.checkArgument(present, "query for your id not exists.");
        appManager.removeAppById(id);
    }

    public AppDtoPlus updateApp(AppDtoPlus appDto) {
        Preconditions.checkNotNull(appDto, "query can not be null.");
        Preconditions.checkNotNull(appDto.getId(), "query id can not be null. ");
        boolean present = appManager.getAppByAutoIncreId(appDto.getId()).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("App for appId=%s not exists", appDto.getId());
        }
        return appManager.updateApp(appDto);
    }

    public PageDto<AppDtoPlus> getAppPlusByConditions(int page, int size, AppQuery appQuery) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");

        PageRequest pageable = new PageRequest(page, size);

        return appManager.getAppPlusByConditions(appQuery, pageable);
    }

    //模糊匹配，根据app name 查询app
    public List<AppDto> fuzzyGetAppsByAppName(String name) {
        if (name == null) {
            name = "";
        }
        return appManager.fuzzyGetAppsByAppName(name);
    }

    public List<AppDto> findAppByAppName(String appName) {
        return appManager.findAppByAppName(appName);
    }


    //给应用在dev,fat分别创建C规格的2个配额
    public void allocateDefaultAppQuota(AppDto appDto) {

        List<EnvDto> envDtos = envManager.getAllEnvs().stream().filter(item -> item.getName().equals("dev") || item.getName().equals("fat")).collect(Collectors.toList());
        SpecTypeDto specTypeByName = specTypeManager.getSpecTypeByName("C-2C4G");

        AppQuotaDto appQuotaDto = new AppQuotaDto();
        appQuotaDto.setAppName(appDto.getName());
        appQuotaDto.setOrgId(appDto.getOrgId());
        appQuotaDto.setNumber(2L);
        appQuotaDto.setSpectypeId(specTypeByName.getId());

        //dev分配
        appQuotaDto.setEnvId(envDtos.get(0).getId());
        appQuotaManager.addAppQuota(appQuotaDto);

        //fat分配
        appQuotaDto.setEnvId(envDtos.get(1).getId());
        appQuotaManager.addAppQuota(appQuotaDto);
    }

    //查询某人负责的应用
    public List<AppDtoPlus> getAppsByUserName(String username) {
        Objects.requireNonNull(username, "username must not be null");
        if ("".equals(username)) {
            throw new AtlasServiceException("username can not be empty");
        }
        Optional<UserDto> userDto = userManager.getByUserName(username);
        if (userDto.isPresent()) {
            String workNumber = userDto.get().getWorkNumber();
            if (workNumber == null || "".equals(workNumber)) {
                return new ArrayList<>();
            }
            return appManager.getAppsByWorkNumber(workNumber);
        } else {
            throw new AtlasServiceException("user not exists for username " + username);
        }

    }

    public Boolean updateAppManagers(AppManagerQuery appManagerQuery) {
        String appId = appManagerQuery.getAppId();
        Objects.requireNonNull(appId, "appId must not be null");

        String developers = appManagerQuery.getDevelopers();
        String testers = appManagerQuery.getTesters();
        Optional<AppDtoPlus> app = appManager.getAppPlusByAppId(appId);

        if (!app.isPresent()) {
            throw new AtlasServiceException("app not exist for appId " + appId);
        }
        if (developers == null || "".equals(developers.trim())) {
            throw new AtlasServiceException("developers can not be empty");
        }

        String[] usernameArray = developers.trim().split(",");

        List<String> usernameStrList = Arrays.stream(usernameArray).map(item -> {
            Optional<UserDto> userDto = userManager.getByUserName(item);
            if (!userDto.isPresent()) {
                throw new AtlasServiceException("user not exists for username " + item);
            }
            return userDto.get().getWorkNumber();
        }).collect(Collectors.toList());

        String usernameWorkNumbers = StringUtils.join(usernameStrList, ",");

        String[] testerArray = null;
        List<String> testStrList = null;
        if (testers != null && !"".equals(testers)) {
            testerArray = testers.trim().split(",");
            testStrList = Arrays.stream(testerArray).map(item -> {
                Optional<UserDto> userDto = userManager.getByUserName(item);
                if (!userDto.isPresent()) {
                    throw new AtlasServiceException("user not exists for username " + item);
                }
                return userDto.get().getWorkNumber();
            }).collect(Collectors.toList());
        }

        String testWorkNumbers = StringUtils.join(testStrList, ",");

        app.get().setDevelopers(usernameWorkNumbers);
        app.get().setTests(testWorkNumbers);

        updateApp(app.get());
        return true;
    }

}
