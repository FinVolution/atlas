package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.AppQuotaDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.query.AppQuotaQuery;
import com.ppdai.atlas.manager.AppQuotaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AppQuotaService {

    @Autowired
    private AppQuotaManager appQuotaManager;

    @Transactional
    public void addAppQuota(AppQuotaDto quotaDto) {
        Preconditions.checkNotNull(quotaDto, "appQuotaDto must not be null.");
        appQuotaManager.addAppQuota(quotaDto);
    }

    public List<AppQuotaDto> getAllAppQuotas() {
        return appQuotaManager.getAllAppQuotas();
    }

    public PageDto<AppQuotaDto> getAppQuotasByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return appQuotaManager.getAppQuotaByPage(page, size);
    }

    @Transactional
    public void removeAppQuotaById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = appQuotaManager.getAppQuotaById(id).isPresent();
        if (!present)
            throw AtlasServiceException.newAtlasException("AppQuotaDto for Id=%s not exists", id);
        appQuotaManager.removeAppQuotaById(id);
    }

    @Transactional
    public void updateAppQuota(AppQuotaDto quotaDto) {
        Preconditions.checkNotNull(quotaDto, "quota can not be null.");
        Preconditions.checkNotNull(quotaDto.getId(), "quota id can not be null. ");
        boolean present = appQuotaManager.getAppQuotaById(quotaDto.getId()).isPresent();
        if (!present)
            throw AtlasServiceException.newAtlasException("quotaDto for Id=%s not exists", quotaDto.getId());
        appQuotaManager.updateAppQuota(quotaDto);
    }

    public PageDto<AppQuotaDto> findAppQuotaByCondition(AppQuotaQuery appQuotaQuery, int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return appQuotaManager.findAppQuotaByCondition(appQuotaQuery, new PageRequest(page, size));
    }

    public void initAppQuotas(String appId) {
        if (appId == null) {
            appQuotaManager.initAppQuotas();
        } else {
            appQuotaManager.initAppQuotas(appId);
        }
    }

    public List<AppQuotaDto> findAppQuotasByAppIdAndEnvName(String appId, String envName) {
        Objects.nonNull(appId);
        Objects.nonNull(envName);
        return appQuotaManager.findAppQuotasByAppIdAndEnvName(appId, envName);
    }


    public AppQuotaDto findAppQuotaByAppIdEnvNameAndSpecName(String appId, String envName, String specName) {
        AppQuotaDto appQuotaDto = appQuotaManager.findAppQuotaByAppIdEnvNameSpecName(appId, envName, specName);
        return appQuotaDto;
    }
}
