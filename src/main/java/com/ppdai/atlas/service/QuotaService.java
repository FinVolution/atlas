package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.QuotaDtoPlus;
import com.ppdai.atlas.dto.query.QuotaQuery;
import com.ppdai.atlas.manager.QuotaManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class QuotaService {

    private QuotaManager quotaManager;

    @Autowired
    public QuotaService(QuotaManager userManager) {
        this.quotaManager = userManager;
    }

    public Optional<QuotaDtoPlus> getQuotaById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null. ");
        return quotaManager.getQuotaById(id);
    }

    @Transactional
    public QuotaDtoPlus addQuota(QuotaDtoPlus quotaDtoPlus) {
        Preconditions.checkNotNull(quotaDtoPlus, "quotaDtoPlus must not be null.");
        return quotaManager.addQuota(quotaDtoPlus);
    }

    public List<QuotaDtoPlus> getAllQuotas() {
        return quotaManager.getAllQuotas();
    }

    public PageDto<QuotaDtoPlus> getQuotasByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return quotaManager.getQuotaByPage(page, size);
    }

    public PageDto<QuotaDtoPlus> getQuotasByCondition(QuotaQuery quotaQuery, int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        PageRequest pageRequest = new PageRequest(page, size);
        return quotaManager.getQuotaByCondition(quotaQuery, pageRequest);
    }

    @Transactional
    public void removeQuotaById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = quotaManager.getQuotaById(id).isPresent();
        if (!present)
            throw AtlasServiceException.newAtlasException("quotaDto for Id=%s not exists", id);
        quotaManager.removeQuotaById(id);
    }

    @Transactional
    public QuotaDtoPlus updateQuota(QuotaDtoPlus quotaDtoPlus) {
        Preconditions.checkNotNull(quotaDtoPlus, "quota can not be null.");
        Preconditions.checkNotNull(quotaDtoPlus.getId(), "quota id can not be null. ");
        boolean present = quotaManager.getQuotaById(quotaDtoPlus.getId()).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("quotaDtoPlus for Id=%s not exists", quotaDtoPlus.getId());
        }
        return quotaManager.addQuota(quotaDtoPlus);
    }

    public QuotaDtoPlus getQuotaByOrgEnvSpecPlus(String orgCode, String envName, String specName) {
        return quotaManager.getQuotaByOrgEnvSpecPlus(orgCode, envName, specName);
    }
}
