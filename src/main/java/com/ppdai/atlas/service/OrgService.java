package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.OrgDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.query.OrgQuery;
import com.ppdai.atlas.manager.OrgManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class OrgService {

    private final OrgManager orgManager;

    @Autowired
    public OrgService(OrgManager orgManager) {
        this.orgManager = orgManager;
    }

    public Optional<OrgDto> getOrgById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        return orgManager.getOrgById(id);
    }

    @Transactional
    public OrgDto addOrg(OrgDto orgDto) {
        Preconditions.checkNotNull(orgDto, "org must not be null.");
        return orgManager.addOrg(orgDto);
    }

    public List<OrgDto> getAllOrgs() {
        return orgManager.getAllOrgs();
    }

    public PageDto<OrgDto> getOrgsByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return orgManager.getOrgByPage(page, size);
    }

    public PageDto<OrgDto> getOrgsByCondition(int page, int size, OrgQuery orgQuery) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        PageRequest pageRequest = new PageRequest(page, size);
        return orgManager.getOrgByCondition(orgQuery, pageRequest);
    }

    @Transactional
    public void removeOrgById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = orgManager.getOrgById(id).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("orgDto for Id=%s not exists", id);
        }
        orgManager.removeOrgById(id);
    }

    @Transactional
    public OrgDto updateOrg(OrgDto orgDto) {
        Preconditions.checkNotNull(orgDto, "orgDto can not be null.");
        Preconditions.checkNotNull(orgDto.getId(), "org id can not be null. ");
        boolean present = orgManager.getOrgById(orgDto.getId()).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("orgDto for Id=%s not exists", orgDto.getId());
        }
        return orgManager.addOrg(orgDto);
    }
}
