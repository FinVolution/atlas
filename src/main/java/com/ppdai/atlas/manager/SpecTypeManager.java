package com.ppdai.atlas.manager;

import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.SpecTypeRepository;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.SpecTypeDto;
import com.ppdai.atlas.dto.query.SpecTypeQuery;
import com.ppdai.atlas.entity.SpecTypeEntity;
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


@Slf4j
@Component
public class SpecTypeManager {

    private SpecTypeRepository specTypeRepository;

    @Autowired
    public SpecTypeManager(SpecTypeRepository specTypeRepository) {
        this.specTypeRepository = specTypeRepository;
    }

    public Optional<SpecTypeDto> getSpecTypeById(Long id) {
        SpecTypeEntity specTypeEntity = specTypeRepository.findOneById(id);
        return Optional.ofNullable(specTypeEntity).map(m -> ConvertUtils.convert(m, SpecTypeDto.class));
    }

    @Transactional
    public SpecTypeDto addSpecType(SpecTypeDto specTypeDto) {
        SpecTypeEntity specTypeEntity = ConvertUtils.convert(specTypeDto, SpecTypeEntity.class);
        specTypeEntity = specTypeRepository.save(specTypeEntity);
        return ConvertUtils.convert(specTypeEntity, SpecTypeDto.class);
    }

    public SpecTypeDto getSpecTypeByName(String specName) {
        SpecTypeEntity specTypeEntity = specTypeRepository.findOneByName(specName);
        if (specTypeEntity == null) {
            return null;
        }
        return ConvertUtils.convert(specTypeEntity, SpecTypeDto.class);
    }

    public List<SpecTypeDto> getAllSpecType() {
        List<SpecTypeEntity> specTypeEntities = specTypeRepository.findAll();
        return ConvertUtils.convert(specTypeEntities, SpecTypeDto.class);
    }

    public PageDto<SpecTypeDto> getSpecTypeByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<SpecTypeEntity> specTypeEntities = specTypeRepository.findAll(pageRequest);
        return ConvertUtils.convertPage(specTypeEntities, SpecTypeDto.class);
    }

    @Transactional
    public void removeSpecById(Long id) {
        specTypeRepository.removeOneEntityById(id);
    }

    public PageDto<SpecTypeDto> getSpecTypeByCondition(SpecTypeQuery specTypeQuery, Pageable pageRequest) {
        SpecTypeQuery query = (specTypeQuery == null) ? new SpecTypeQuery() : specTypeQuery;
        Page<SpecTypeEntity> specTypeEntities = specTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Optional.ofNullable(query.getName()).ifPresent(o -> predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + query.getName() + "%")));
            Optional.ofNullable(query.getCpu()).ifPresent(o -> predicates.add(criteriaBuilder.equal(root.get("cpu").as(Float.class), query.getCpu())));
            Optional.ofNullable(query.getMemory()).ifPresent(o -> predicates.add(criteriaBuilder.equal(root.get("memory").as(Float.class), query.getMemory())));
            Optional.ofNullable(query.getDisk()).ifPresent(o -> predicates.add(criteriaBuilder.equal(root.get("disk").as(Float.class), query.getDisk())));
            predicates.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), true));
            Predicate[] arrayPredicates = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arrayPredicates));
        }, pageRequest);
        PageDto<SpecTypeDto> specTypeDtoPageDto = ConvertUtils.convertPage(specTypeEntities, SpecTypeDto.class);

        return specTypeDtoPageDto;
    }

}
