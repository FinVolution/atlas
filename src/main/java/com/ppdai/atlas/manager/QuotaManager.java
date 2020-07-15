package com.ppdai.atlas.manager;

import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.*;
import com.ppdai.atlas.dto.*;
import com.ppdai.atlas.dto.query.QuotaQuery;
import com.ppdai.atlas.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class QuotaManager {

    @Autowired
    private QuotaRepository quotaRepository;

    @Autowired
    private SpecTypeRepository specTypeRepository;

    @Autowired
    private EnvRepository envRepository;

    @Autowired
    private OrgRepository orgRepository;


    //add specTypeDto to quota
    private QuotaDtoPlus setEnvOrgToQuota(QuotaEntity quotaEntity) {
        QuotaDtoPlus quotaDtoPlus = ConvertUtils.convert(quotaEntity, QuotaDtoPlus.class);

        EnvEntity envEntity = envRepository.findOneById(quotaEntity.getEnvId());
        if (envEntity != null)
            quotaDtoPlus.setEnvDto(ConvertUtils.convert(envEntity, EnvDto.class));

        OrgEntity orgEntity = orgRepository.findOneById(quotaEntity.getOrgId());
        if (orgEntity != null)
            quotaDtoPlus.setOrgDto(ConvertUtils.convert(orgEntity, OrgDto.class));

        return quotaDtoPlus;
    }

    public Optional<QuotaDtoPlus> getQuotaById(Long id) {
        QuotaEntity quotaEntity = quotaRepository.findOneById(id);
        return Optional.ofNullable(quotaEntity).map(m -> setEnvOrgToQuota(m));
    }

    @Transactional
    public QuotaDtoPlus addQuota(QuotaDtoPlus quotaDtoPlus) {
        QuotaEntity quotaEntity = ConvertUtils.convert(quotaDtoPlus, QuotaEntity.class);
        OrgEntity orgEntity = orgRepository.findOneById(quotaDtoPlus.getOrgId());
        quotaEntity.setOrgName(orgEntity.getName());
        quotaEntity = quotaRepository.save(quotaEntity);
        quotaRepository.flush();
        quotaDtoPlus = setEnvOrgToQuota(quotaEntity);
        return quotaDtoPlus;
    }

    public List<QuotaDtoPlus> getAllQuotas() {
        List<QuotaEntity> quotaEntities = quotaRepository.findAll();
        List<QuotaDtoPlus> quotaDtoPluses = quotaEntities.stream().map(m -> setEnvOrgToQuota(m)).collect(Collectors.toList());
        return quotaDtoPluses;
    }

    public PageDto<QuotaDtoPlus> getQuotaByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<QuotaEntity> quotaEntities = quotaRepository.findAll(pageRequest);
        PageDto<QuotaDtoPlus> quotaDtoPageDto = ConvertUtils.convertPage(quotaEntities, QuotaDtoPlus.class);
        //add specTypeDto to every quotaDto
        List<QuotaDtoPlus> quotaDtoPluses = quotaEntities.getContent().stream().map(m -> setEnvOrgToQuota(m)).collect(Collectors.toList());
        //set quotaDto to page
        quotaDtoPageDto.setContent(quotaDtoPluses);
        return quotaDtoPageDto;
    }


    /**
     * 新版本方法，返回值类型为 QuotaDtoPlus, 适配新表字段
     * @param orgCode
     * @param envName
     * @param specName
     * @return
     */
    public QuotaDtoPlus getQuotaByOrgEnvSpecPlus(String orgCode, String envName, String specName) {

        OrgEntity orgEntity = orgRepository.findOrgByOrgCode(orgCode);
        EnvEntity envEntity = envRepository.findOneByName(envName);
        SpecTypeEntity specTypeEntity = specTypeRepository.findOneByName(specName);

        if (orgEntity == null || envEntity == null || specTypeEntity == null) {
            return null;
        }

        QuotaEntity quotaEntity = quotaRepository.findOneByOrgEnv(orgEntity.getId(), envEntity.getId());
        QuotaDtoPlus quotaDtoPlus = setEnvOrgToQuota(quotaEntity);

        return quotaDtoPlus;

    }

    @Transactional
    public void removeQuotaById(Long id) {
        quotaRepository.removeOneEntityById(id);
    }

    public PageDto<QuotaDtoPlus> getQuotaByCondition(QuotaQuery quotaQuery, Pageable pageRequest) {
        QuotaQuery query = (quotaQuery == null) ? new QuotaQuery() : quotaQuery;
        Page<QuotaEntity> envEntityPage = quotaRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Optional.ofNullable(query.getOrgName()).ifPresent(o -> predicates.add(criteriaBuilder.like(root.get("orgName").as(String.class), "%" + query.getOrgName() + "%")));
            Optional.ofNullable(query.getEnvId()).ifPresent(o -> predicates.add(criteriaBuilder.equal(root.get("envId").as(Long.class), query.getEnvId())));
            predicates.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), true));
            Predicate[] arrayPredicates = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arrayPredicates));
        }, pageRequest);
        PageDto<QuotaDtoPlus> quotaDtoPageDto = ConvertUtils.convertPage(envEntityPage, QuotaDtoPlus.class);
        List<QuotaDtoPlus> quotaDtoPluses = envEntityPage.getContent().stream().map(m -> setEnvOrgToQuota(m)).collect(Collectors.toList());
        quotaDtoPageDto.setContent(quotaDtoPluses);
        return quotaDtoPageDto;
    }

}
