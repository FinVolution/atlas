package com.ppdai.atlas.manager;

import com.google.common.collect.Lists;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.AppRepository;
import com.ppdai.atlas.dao.OrgRepository;
import com.ppdai.atlas.dao.RoleRepository;
import com.ppdai.atlas.dao.UserRepository;
import com.ppdai.atlas.dto.*;
import com.ppdai.atlas.dto.query.AppQuery;
import com.ppdai.atlas.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AppManager {

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private UserExtManager userExtManager;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 老接口，只为app提供一个负责人
     *
     * @param appEntity
     * @return
     */
    private AppDto setOrgAndUserToApp(AppEntity appEntity) {

        AppDto appDto = ConvertUtils.convert(appEntity, AppDto.class);

        //添加org
        OrgEntity orgEntity = orgRepository.findOneById(appEntity.getOrgId());
        Optional<OrgDto> orgDtoOptional = Optional.ofNullable(orgEntity).map(item -> ConvertUtils.convert(item, OrgDto.class));
        appDto.setOrgDto(orgDtoOptional.orElse(null));

        //添加user
        String userWorkNumbers = appEntity.getDevelopers();
        String[] workNumberArray = userWorkNumbers.split(",");

        //从所有非null用户中获取第一个user

        UserEntity firstUser = null;

        for (String item : workNumberArray) {
            firstUser = userExtManager.findOneByWorkNumber(item);
            if (firstUser != null) {
                break;
            }
        }

        if (firstUser != null) {
            appDto.setUserDto(ConvertUtils.convert(firstUser, UserDto.class));
        }

        return appDto;
    }

    /**
     * 新接口，为app设置多负责人
     *
     * @param appEntity
     * @return
     */
    private AppDtoPlus setOrgAndUserToAppPlus(AppEntity appEntity) {
        AppDtoPlus appDtoPlus = ConvertUtils.convert(appEntity, AppDtoPlus.class);

        //添加org
        OrgEntity orgEntity = orgRepository.findOneById(appEntity.getOrgId());
        Optional<OrgDto> orgDtoOptional = Optional.ofNullable(orgEntity).map(item -> ConvertUtils.convert(item, OrgDto.class));
        appDtoPlus.setOrgDto(orgDtoOptional.orElse(null));

        //添加dev user
        String userWorkNumbers = appEntity.getDevelopers();
        String[] workNumberArray = userWorkNumbers.split(",");

        //去除null负责人
        List<UserEntity> userEntities = Arrays.stream(workNumberArray).map(item -> userExtManager.findOneByWorkNumber(item)).filter(Objects::nonNull).collect(Collectors.toList());

        List<UserDto> userDtos = userEntities.stream().map(item ->
        {
            Optional<UserDto> dto = Optional.ofNullable(item).map(entity -> ConvertUtils.convert(entity, UserDto.class));
            return dto.orElse(null);

        }).collect(Collectors.toList());

        appDtoPlus.setUserDtos(userDtos);

        //添加test user
        String testWorkNumber = appEntity.getTests();
        if (testWorkNumber == null || "".equals(testWorkNumber)) {
            appDtoPlus.setTests("");
            appDtoPlus.setTestUserDtos(new ArrayList<>());
            return appDtoPlus;
        }
        String[] testWorkNumberArray = testWorkNumber.split(",");
        List<UserEntity> testUserEntities = Arrays.stream(testWorkNumberArray).map(item -> userExtManager.findOneByWorkNumber(item)).filter(Objects::nonNull).collect(Collectors.toList());

        List<UserDto> testUserDtos = testUserEntities.stream().map(item ->
        {
            Optional<UserDto> dto = Optional.ofNullable(item).map(entity -> ConvertUtils.convert(entity, UserDto.class));
            return dto.orElse(null);

        }).collect(Collectors.toList());

        appDtoPlus.setTestUserDtos(testUserDtos);
        return appDtoPlus;
    }

    public Optional<AppDto> getAppByAutoIncreId(Long id) {
        AppEntity appEntity = appRepository.findOneByAutoIncreId(id);
        return Optional.ofNullable(appEntity).map(m -> setOrgAndUserToApp(m));
    }

    public Optional<AppDto> getAppByAppId(String appId) {
        AppEntity appEntity = appRepository.findOneByAppId(appId);
        return Optional.ofNullable(appEntity).map(m -> setOrgAndUserToApp(m));
    }

    public Optional<AppDtoPlus> getAppPlusByAppId(String appId) {
        AppEntity appEntity = appRepository.findOneByAppId(appId);
        return Optional.ofNullable(appEntity).map(m -> setOrgAndUserToAppPlus(m));
    }

    /**
     * 添加唯一索引后的添加方法
     *
     * @param appDto
     * @return
     */
    @Transactional
    public AppDtoPlus addApp(AppDtoPlus appDto) {
        String appName = appDto.getName();
        String appId = appDto.getAppId();

        //新建时保证 appName 和 appId 不重复
        AppEntity app = appRepository.findAppByNameNotCareActive(appName);
        AppEntity app2 = appRepository.findOneByAppIdNoCareActive(appId);

        if (app == null && app2 == null) {
            //直接插入
            AppEntity appEntity = ConvertUtils.convert(appDto, AppEntity.class);
            appEntity = appRepository.save(appEntity);
            return setOrgAndUserToAppPlus(appEntity);
        } else if (app != null && app2 == null) {
            //appName存在
            //已存在有效值
            if (app.isActive()) {
                throw new AtlasServiceException("appName=" + appName + " already exists");
            } else {
                //更新无效值
                AppEntity appEntity = ConvertUtils.convert(appDto, AppEntity.class);
                appEntity.setId(app.getId());
                appEntity.setActive(true);
                appRepository.save(appEntity);
                return setOrgAndUserToAppPlus(appEntity);
            }
        } else {
            //appId存在
            if (app2.isActive()) {
                throw new AtlasServiceException("appId=" + appId + " already exists");
            } else {
                //更新无效值
                AppEntity appEntity = ConvertUtils.convert(appDto, AppEntity.class);
                appEntity.setId(app2.getId());
                appEntity.setActive(true);
                appRepository.save(appEntity);
                return setOrgAndUserToAppPlus(appEntity);
            }
        }

    }

    /**
     * 添加唯一索引后的更新方法
     */
    @Transactional
    public AppDtoPlus updateApp(AppDtoPlus appDto) {
        String appName = appDto.getName();

        //更新时保证appName不重复，appId由数据库处理
        AppEntity app = appRepository.findAppByNameNotCareActive(appName);

        if (app == null) {
            //直接更新
            AppEntity appEntity = ConvertUtils.convert(appDto, AppEntity.class);
            appEntity = appRepository.save(appEntity);
            return setOrgAndUserToAppPlus(appEntity);
        } else {

            //1)isActive=false
            //2)isActive=true,其他应用的name,重复
            //3)isActive=true,自己这个应用,name未变动
            if (!app.isActive()) {
                AppEntity appEntity = ConvertUtils.convert(appDto, AppEntity.class);
                //将旧的软删数据恢复
                appEntity.setId(app.getId());
                appEntity.setActive(true);
                appEntity = appRepository.save(appEntity);
                return setOrgAndUserToAppPlus(appEntity);
            }

            //存在其他有效重名数据
            if (app.isActive() && app.getId().longValue() != appDto.getId().longValue()) {
                throw new AtlasServiceException("appName=" + appName + " already exists");
            }

            //本身name未更改
            AppEntity appEntity = ConvertUtils.convert(appDto, AppEntity.class);
            appEntity = appRepository.save(appEntity);
            return setOrgAndUserToAppPlus(appEntity);

        }
    }

    public List<AppDto> getAllApps(Long orgId) {
        List<AppEntity> appEntities;
        if (orgId == null) {
            appEntities = appRepository.findAll();
        } else {
            appEntities = appRepository.findAllByOrgId(orgId);
        }
        List<AppDto> appDtos = appEntities.stream().map(m -> setOrgAndUserToApp(m)).collect(Collectors.toList());
        return appDtos;
    }

    public List<AppDtoPlus> getAllAppPlus(Long orgId) {
        List<AppEntity> appEntities;
        if (orgId == null) {
            appEntities = appRepository.findAll();
        } else {
            appEntities = appRepository.findAllByOrgId(orgId);
        }

        List<AppDtoPlus> appDtos = appEntities.stream().map(m -> setOrgAndUserToAppPlus(m)).collect(Collectors.toList());
        return appDtos;
    }

    public PageDto<AppDto> getAppByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<AppEntity> appEntities = appRepository.findAll(pageRequest);

        //转成AppDto集合
        List<AppDto> appDtos = appEntities.getContent().stream().map(m -> setOrgAndUserToApp(m)).collect(Collectors.toList());

        PageDto<AppDto> appDtoPageDto = ConvertUtils.convertPage(appEntities, AppDto.class);
        appDtoPageDto.setContent(appDtos);
        return appDtoPageDto;
    }

    public PageDto<AppDtoPlus> getAppPlusByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<AppEntity> appEntities = appRepository.findAll(pageRequest);

        //转成AppDto集合
        List<AppDtoPlus> appDtos = appEntities.getContent().stream().map(m -> setOrgAndUserToAppPlus(m)).collect(Collectors.toList());

        PageDto<AppDtoPlus> appDtoPageDto = ConvertUtils.convertPage(appEntities, AppDtoPlus.class);
        appDtoPageDto.setContent(appDtos);

        return appDtoPageDto;
    }

    public List<AppDto> fuzzyGetAppsByAppName(String appName) {
        List<AppEntity> appEntities = appRepository.fuzzyFindByAppName(appName);
        return appEntities.stream().map(m -> setOrgAndUserToApp(m)).collect(Collectors.toList());
    }

    //兼容旧接口
    public List<AppDto> findAppByAppName(String appName) {
        AppEntity appEntity = appRepository.findAppByAppName(appName);
        AppDto appDto = setOrgAndUserToApp(appEntity);
        return Arrays.asList(appDto);
    }

    @Transactional
    public void removeAppById(Long id) {
        appRepository.removeOneEntityById(id);
    }

    public PageDto<AppDtoPlus> getAppPlusByConditions(AppQuery appQueryConditionDto, PageRequest page) {
        AppQuery condition = appQueryConditionDto == null ? new AppQuery() : appQueryConditionDto;
        Page<AppEntity> appEntities = appRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(condition.getOrgId()).ifPresent(o ->
                    predicates.add(criteriaBuilder.equal(root.get("orgId").as(Long.class), condition.getOrgId())));
            Optional.ofNullable(condition.getAppId()).ifPresent(o ->
                    {
                        if (!"".equals(condition.getAppId().trim())) {
                            predicates.add(criteriaBuilder.equal(root.get("appId").as(String.class), condition.getAppId().trim()));
                        }
                    }
            );

            Optional.ofNullable(condition.getAppName()).ifPresent(o ->
                    {
                        if (!"".equals(condition.getAppName().trim())) {
                            predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + condition.getAppName().trim() + "%"));
                        }

                    }

            );

            Optional.ofNullable(condition.getDevelopers()).ifPresent(o ->
                    {
                        if (!"".equals(condition.getDevelopers())) {
                            predicates.add(criteriaBuilder.like(root.get("developers").as(String.class), "%" + condition.getDevelopers() + "%"));
                        }
                    }
            );

            predicates.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), true));
            Predicate[] arrayPredicates = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arrayPredicates));
        }, page);

        //转成AppDto集合
        List<AppDtoPlus> appDtos = appEntities.getContent().stream().map(m -> setOrgAndUserToAppPlus(m)).collect(Collectors.toList());

        PageDto<AppDtoPlus> appDtoPageDto = ConvertUtils.convertPage(appEntities, AppDtoPlus.class);
        appDtoPageDto.setContent(appDtos);

        return appDtoPageDto;
    }


    //查询某个所负责的所有应用
    public List<AppDtoPlus> getAppsByWorkNumber(String worknumber) {
        List<AppEntity> apps = appRepository.findAppsByUserWorkNumber(worknumber);
        return apps.stream().map(m -> setOrgAndUserToAppPlus(m)).collect(Collectors.toList());
    }

    public List<Long> loadRoleIds(List<String> roleNames) {
        if (roleNames == null || roleNames.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        List<RoleEntity> roleEntities = roleNames.stream().map(item -> roleRepository.findOneByName(item)).filter(item-> item != null).collect(Collectors.toList());
        if (roleEntities == null || roleEntities.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        List<Long> idList = roleEntities.stream().map(item -> item.getId()).collect(Collectors.toList());
        return idList;
    }

}
