package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.RoleDto;
import com.ppdai.atlas.dto.query.RoleQuery;
import com.ppdai.atlas.manager.RoleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RoleService {

    private RoleManager roleManager;

    @Autowired
    public RoleService(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public Optional<RoleDto> getRoleById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null. ");
        return roleManager.getRoleById(id);
    }

    @Transactional
    public RoleDto addRole(RoleDto roleDto) {
        Preconditions.checkNotNull(roleDto, "role must not be null.");
        return roleManager.addRole(roleDto);
    }

    public List<RoleDto> getAllRoles() {
        return roleManager.getAllRoles();
    }

    public PageDto<RoleDto> getRolesByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return roleManager.getRoleByPage(page, size);
    }

    public PageDto<RoleDto> getRolesByCondition(int page, int size, RoleQuery roleQuery) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        PageRequest pageRequest = new PageRequest(page, size);
        return roleManager.getRolesByCondition(roleQuery, pageRequest);
    }

    @Transactional
    public void removeRoleById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = roleManager.getRoleById(id).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("roleDto for Id=%s not exists", id);
        }
        roleManager.removeRoleById(id);
    }

    @Transactional
    public RoleDto updateRole(RoleDto roleDto) {
        Preconditions.checkNotNull(roleDto, "roleDto can not be null.");
        Preconditions.checkNotNull(roleDto.getId(), "roleDto id can not be null. ");
        boolean present = roleManager.getRoleById(roleDto.getId()).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("roleDto for Id=%s not exists", roleDto.getId());
        }
        return roleManager.addRole(roleDto);
    }

}
