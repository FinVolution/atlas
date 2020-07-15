package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.SpecTypeDto;
import com.ppdai.atlas.dto.query.SpecTypeQuery;
import com.ppdai.atlas.manager.SpecTypeManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class SpecTypeService {
    private SpecTypeManager specTypeManager;

    @Autowired
    public SpecTypeService(SpecTypeManager specTypeManager) {
        this.specTypeManager = specTypeManager;
    }

    public Optional<SpecTypeDto> getSpecTypeById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null. ");
        return specTypeManager.getSpecTypeById(id);
    }

    public SpecTypeDto getSpecTypeByName(String specName) {
        return specTypeManager.getSpecTypeByName(specName);
    }

    @Transactional
    public SpecTypeDto addSpecType(SpecTypeDto specTypeDto) {
        Preconditions.checkNotNull(specTypeDto, "specTypeDto must not be null.");
        return specTypeManager.addSpecType(specTypeDto);
    }

    public List<SpecTypeDto> getAllSpecTypes() {
        return specTypeManager.getAllSpecType();
    }

    public PageDto<SpecTypeDto> getSpecTypesByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return specTypeManager.getSpecTypeByPage(page, size);
    }

    @Transactional
    public void removeSpecTypeById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = specTypeManager.getSpecTypeById(id).isPresent();
        if (!present)
            throw AtlasServiceException.newAtlasException("specTypeDto for Id=%s not exists", id);
        specTypeManager.removeSpecById(id);
    }

    @Transactional
    public SpecTypeDto updateSpecType(SpecTypeDto specTypeDto) {
        Preconditions.checkNotNull(specTypeDto, "specTypeDto can not be null.");
        Preconditions.checkNotNull(specTypeDto.getId(), "specTypeDto id can not be null. ");
        boolean present = specTypeManager.getSpecTypeById(specTypeDto.getId()).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("specTypeDto for Id=%s not exists", specTypeDto.getId());
        }
        return specTypeManager.addSpecType(specTypeDto);
    }

    public PageDto<SpecTypeDto> getSpecTypesByCondition(int page, int size, SpecTypeQuery specTypeQuery) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        PageRequest pageRequest = new PageRequest(page, size);
        return specTypeManager.getSpecTypeByCondition(specTypeQuery, pageRequest);
    }

}
