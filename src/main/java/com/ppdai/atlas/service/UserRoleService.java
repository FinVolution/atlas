package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.RoleDto;
import com.ppdai.atlas.dto.UserRoleDto;
import com.ppdai.atlas.manager.UserRoleManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class UserRoleService {

    private final UserRoleManager userRoleManager;

    @Autowired
    public UserRoleService(UserRoleManager userRoleManager) {
        this.userRoleManager = userRoleManager;
    }

    public Optional<UserRoleDto> getUserRoleById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null. ");
        return userRoleManager.getUserRoleById(id);
    }

    @Transactional
    public List<UserRoleDto> addUserRole(String userWorkNumber, List<Long> roleIds) {
        Preconditions.checkNotNull(userWorkNumber, "userWorkNumber must not be null.");
        Preconditions.checkNotNull(roleIds, "roleIds must not be null.");
        return userRoleManager.addUserRole(userWorkNumber, roleIds);
    }

    @Transactional
    public UserRoleDto addUserRole(UserRoleDto userRoleDto) {
        Preconditions.checkNotNull(userRoleDto, "userRoleDto must not be null.");
        return userRoleManager.addUserRole(userRoleDto);
    }


    public List<UserRoleDto> getAllUserRoles() {
        return userRoleManager.getAllUserRoless();
    }

    public PageDto<UserRoleDto> getUserRolesByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return userRoleManager.getUserRoleByPage(page, size);
    }

    @Transactional
    public void removeUserRoleById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = userRoleManager.getUserRoleById(id).isPresent();
        if (!present)
            throw AtlasServiceException.newAtlasException("userRoleDto for Id=%s not exists", id);
        userRoleManager.removeUserRoleById(id);
    }

    @Transactional
    public UserRoleDto updateUserRole(UserRoleDto userRoleDto) {
        Preconditions.checkNotNull(userRoleDto, "userRole can not be null.");
        Preconditions.checkNotNull(userRoleDto.getId(), "user id can not be null. ");
        boolean present = userRoleManager.getUserRoleById(userRoleDto.getId()).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("userRoleDto for Id=%s not exists", userRoleDto.getId());
        }
        return userRoleManager.addUserRole(userRoleDto);
    }

    public List<RoleDto> getUserRolesByUserWorkNumber(String userWorkNumber) {
        Objects.requireNonNull(userWorkNumber, "user id can not be null");
        List<RoleDto> roleDtos = userRoleManager.getRolesByUserWorkNumber(userWorkNumber);
        return roleDtos;
    }

    public List<String> getUserRolesByUserName(String userName) {
        Objects.requireNonNull(userName, "user id can not be null");
        List<String> roles = userRoleManager.getRolesByUserName(userName);
        return roles;
    }

}
