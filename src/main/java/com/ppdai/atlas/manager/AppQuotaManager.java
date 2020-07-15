package com.ppdai.atlas.manager;


import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.*;
import com.ppdai.atlas.dto.AppQuotaDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.query.AppQuotaQuery;
import com.ppdai.atlas.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
public class AppQuotaManager {

    @Autowired
    private AppQuotaRepository appQuotaRepository;

    @Autowired
    private SpecTypeRepository specTypeRepository;

    @Autowired
    private EnvRepository envRepository;

    @Autowired
    private AppRepository appRepository;

    private AppQuotaDto setAppEnvSpectypeToQuota(AppQuotaEntity appQuotaEntity) {

        AppQuotaDto appQuotaDto = ConvertUtils.convert(appQuotaEntity, AppQuotaDto.class);

        EnvEntity envEntity = envRepository.findOneById(appQuotaEntity.getEnvId());
        if (envEntity != null) {
            appQuotaDto.setEnvName(envEntity.getName());
        }
        AppEntity appEntity = appRepository.findOneByAppId(appQuotaEntity.getAppId());
        if (appEntity != null) {
            appQuotaDto.setAppName(appEntity.getName());
        }
        SpecTypeEntity specTypeEntity = specTypeRepository.findOneById(appQuotaEntity.getSpectypeId());

        if (specTypeEntity != null) {
            appQuotaDto.setSpectypeName(specTypeEntity.getName());
        }

        return appQuotaDto;
    }

    public Optional<AppQuotaDto> getAppQuotaById(Long id) {
        AppQuotaEntity quotaEntity = appQuotaRepository.findOneById(id);
        return Optional.ofNullable(quotaEntity).map(m -> setAppEnvSpectypeToQuota(m));
    }

    /**
     * 新增app-quota
     *
     * @param appQuotaDto
     * @return
     */
    @Transactional
    public void addAppQuota(AppQuotaDto appQuotaDto) {
        //按应用名查找app
        AppEntity appEntity = appRepository.findAppByAppName(appQuotaDto.getAppName());

        //判断应用存在与否
        if (appEntity == null) {
            throw new AtlasServiceException("应用不存在");
        }

        EnvEntity envEntity = envRepository.findOneById(appQuotaDto.getEnvId());
        if (envEntity == null) {
            throw new AtlasServiceException("环境不存在");
        }

        SpecTypeEntity specTypeEntity = specTypeRepository.findOneById(appQuotaDto.getSpectypeId());
        if (specTypeEntity == null) {
            throw new AtlasServiceException("规格不存在");
        }

        appQuotaDto.setAppName(appEntity.getName());
        appQuotaDto.setAppId(appEntity.getAppId());
        appQuotaDto.setOrgId(appEntity.getOrgId());

        AppQuotaEntity appQuota = appQuotaRepository.findOneByAppEnvSpec(appQuotaDto.getAppId(), appQuotaDto.getEnvId(), appQuotaDto.getSpectypeId());

        //app-env-spec 是否存在
        if (appQuota != null) {
            throw new AtlasServiceException("应用： " + appEntity.getName() + ", 环境：" + envEntity.getName() + ", 规格：" + specTypeEntity.getName() + "，已经创建配额，请勿重复");
        }

        AppQuotaEntity appQuotaEntity = ConvertUtils.convert(appQuotaDto, AppQuotaEntity.class);

        appQuotaRepository.saveAndFlush(appQuotaEntity);
    }

    /**
     * 编辑app-quota只允许数量上的更新
     *
     * @param appQuotaDto
     * @return
     */
    @Transactional
    public void updateAppQuota(AppQuotaDto appQuotaDto) {
        AppQuotaEntity appQuotaEntity = ConvertUtils.convert(appQuotaDto, AppQuotaEntity.class);

        appQuotaRepository.save(appQuotaEntity);
        appQuotaRepository.flush();
    }




    public List<AppQuotaDto> getAllAppQuotas() {
        List<AppQuotaEntity> appQuotaEntities = appQuotaRepository.findAll();
        List<AppQuotaDto> appQuotaDtos = appQuotaEntities.stream().map(m -> setAppEnvSpectypeToQuota(m)).collect(Collectors.toList());
        return appQuotaDtos;
    }

    public PageDto<AppQuotaDto> getAppQuotaByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<AppQuotaEntity> appQuotaEntities = appQuotaRepository.findAll(pageRequest);
        PageDto<AppQuotaDto> quotaDtoPageDto = ConvertUtils.convertPage(appQuotaEntities, AppQuotaDto.class);
        //add specTypeDto to every quotaDto
        List<AppQuotaDto> quotaDtos = appQuotaEntities.getContent().stream().map(m -> setAppEnvSpectypeToQuota(m)).collect(Collectors.toList());
        //set quotaDto to page
        quotaDtoPageDto.setContent(quotaDtos);
        return quotaDtoPageDto;
    }

    public PageDto<AppQuotaDto> findAppQuotaByCondition(AppQuotaQuery quotaQuery, Pageable pageRequest) {
        AppQuotaQuery query = (quotaQuery == null) ? new AppQuotaQuery() : quotaQuery;
        Page<AppQuotaEntity> appQuotaEntityPage = appQuotaRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Optional.ofNullable(query.getAppId()).ifPresent(o -> {
                        if (StringUtils.isNotEmpty(query.getAppId())) {
                            predicates.add(criteriaBuilder.equal(root.get("appId").as(String.class), query.getAppId()));
                        }
                    }
            );
            Optional.ofNullable(query.getEnvId()).ifPresent(o -> predicates.add(criteriaBuilder.equal(root.get("envId").as(Long.class), query.getEnvId())));

            predicates.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), true));
            Predicate[] arrayPredicates = new Predicate[predicates.size()];

            return criteriaBuilder.and(predicates.toArray(arrayPredicates));
        }, pageRequest);

        PageDto<AppQuotaDto> quotaDtoPageDto = ConvertUtils.convertPage(appQuotaEntityPage, AppQuotaDto.class);
        List<AppQuotaDto> quotaDtos = appQuotaEntityPage.getContent().stream().map(m -> setAppEnvSpectypeToQuota(m)).collect(Collectors.toList());
        quotaDtoPageDto.setContent(quotaDtos);

        return quotaDtoPageDto;
    }

    @Transactional
    public void removeAppQuotaById(Long id) {
        appQuotaRepository.removeOneEntityById(id);
    }

    public List<AppQuotaDto> findAppQuotasByAppIdAndEnvName(String appId, String envName) {
        EnvEntity envEntity = envRepository.findOneByName(envName.trim());
        List<AppQuotaEntity> appQuotas = appQuotaRepository.findAllByAppIdAndEnvId(appId, envEntity.getId());
        List<AppQuotaDto> appQuotaDtos = appQuotas.stream().map(item -> setAppEnvSpectypeToQuota(item)).collect(Collectors.toList());
        return appQuotaDtos;
    }

    public List<AppQuotaEntity> findAppQuotasByAppId(String appId) {
        List<AppQuotaEntity> quotaEntities = appQuotaRepository.findAllAppQuotaByAppId(appId);
        return quotaEntities;
    }

    public AppQuotaDto findAppQuotaByAppIdEnvNameSpecName(String appId, String envName, String specName) {

        EnvEntity envEntity = envRepository.findOneByName(envName.trim());
        SpecTypeEntity specTypeEntity = specTypeRepository.findOneByName(specName.trim());

        Objects.requireNonNull(envEntity, envName + " environment not exist");
        Objects.requireNonNull(specTypeEntity, specName + " spectype not exist");

        AppQuotaEntity appQuotaEntity = appQuotaRepository.findByAppIdEnvIdAndSpecId(appId, envEntity.getId(), specTypeEntity.getId());

        Objects.requireNonNull(appQuotaEntity, appId + ", " + envName + ", " + specName + " appquota not exists");

        AppQuotaDto appQuotaDto = setAppEnvSpectypeToQuota(appQuotaEntity);
        return appQuotaDto;
    }

    public void initAppQuotas(String appId) {
        Long defaultNumber = Long.valueOf(( env.getProperty("app.quota.default.number")));

        AppEntity appEntity = appRepository.findOneByAppId(appId);
        if (appEntity == null) {
            throw new AtlasServiceException("应用不存在");
        }

        List<EnvEntity> envList = envRepository.findAll();
        List<SpecTypeEntity> specList = specTypeRepository.findAll();

        for (int i = 0; i < envList.size(); i++) {
            EnvEntity envEntity = envList.get(i);
            for (int j = 0; j < specList.size(); j++) {
                SpecTypeEntity specTypeEntity = specList.get(j);
                AppQuotaEntity appQuota = appQuotaRepository.findOneByAppEnvSpec(appEntity.getAppId(), envEntity.getId(), specTypeEntity.getId());
                if (appQuota == null) {
                    AppQuotaEntity appQuotaEntity = new AppQuotaEntity();
                    appQuotaEntity.setAppId(appEntity.getAppId());
                    appQuotaEntity.setAppName(appEntity.getName());
                    appQuotaEntity.setOrgId(appEntity.getOrgId());
                    appQuotaEntity.setEnvId(envEntity.getId());
                    appQuotaEntity.setSpectypeId(specTypeEntity.getId());
                    appQuotaEntity.setNumber(defaultNumber);
                    appQuotaRepository.saveAndFlush(appQuotaEntity);
                }
            }
        }
    }
    @Autowired
    private Environment env;



    public void initAppQuotas() {
       // Long defaultNumber =
        Long defaultNumber = Long.valueOf(( env.getProperty("app.quota.default.number").toString() ));

        List<AppEntity> appList = appRepository.findAll();
        List<EnvEntity> envList = envRepository.findAll();
        List<SpecTypeEntity> specList = specTypeRepository.findAll();

        for (int i = 0; i < appList.size(); i++) {
            AppEntity appEntity = appList.get(i);
            for (int j = 0; j < envList.size(); j++) {
                EnvEntity envEntity = envList.get(j);
                for (int k = 0; k < specList.size(); k++) {
                    SpecTypeEntity specTypeEntity = specList.get(k);
                    AppQuotaEntity appQuota = appQuotaRepository.findByAppIdEnvIdAndSpecId(appEntity.getAppId(), envEntity.getId(), specTypeEntity.getId());
                    if (appQuota == null) {
                        AppQuotaEntity appQuotaEntity = new AppQuotaEntity();
                        appQuotaEntity.setAppId(appEntity.getAppId());
                        appQuotaEntity.setAppName(appEntity.getName());
                        appQuotaEntity.setOrgId(appEntity.getOrgId());
                        appQuotaEntity.setEnvId(envEntity.getId());
                        appQuotaEntity.setSpectypeId(specTypeEntity.getId());
                        appQuotaEntity.setNumber(defaultNumber);
                        appQuotaRepository.save(appQuotaEntity);
                    }
                }
            }
        }
    }
}
