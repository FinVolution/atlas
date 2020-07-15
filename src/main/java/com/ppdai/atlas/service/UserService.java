package com.ppdai.atlas.service;

import com.google.common.base.Preconditions;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.UserDto;
import com.ppdai.atlas.dto.query.UserQuery;
import com.ppdai.atlas.manager.UserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
public class UserService {

    @Autowired
    private UserManager userManager;


    public Optional<UserDto> getUserById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null. ");
        return userManager.getUserById(id);
    }

    @Transactional
    public UserDto addUser(UserDto userDto) {
        Preconditions.checkNotNull(userDto, "user must not be null.");
        return userManager.addUser(userDto);
    }

    public List<UserDto> getAllUsers() {
        return userManager.getAllUsers();
    }

    public PageDto<UserDto> getUsersByPage(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        return userManager.getUserByPage(page, size);
    }

    public PageDto<UserDto> getUsersByCondition(UserQuery userQuery, int page, int size) {
        Preconditions.checkArgument(page >= 0, "page must be bigger or equal than 0.");
        Preconditions.checkArgument(size >= 1, "size must be bigger or equal than 1.");
        PageRequest pageRequest = new PageRequest(page, size);
        return userManager.getUserOrgInfoByCondition(userQuery, pageRequest);
    }

    @Transactional
    public void removeUserById(Long id) {
        Preconditions.checkNotNull(id, "id must not be null.");
        boolean present = userManager.getUserById(id).isPresent();
        if (!present)
            throw AtlasServiceException.newAtlasException("userDto for Id=%s not exists", id);
        userManager.removeUserById(id);
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        Preconditions.checkNotNull(userDto, "user can not be null.");
        Preconditions.checkNotNull(userDto.getId(), "user id can not be null. ");
        boolean present = userManager.getUserById(userDto.getId()).isPresent();
        if (!present) {
            throw AtlasServiceException.newAtlasException("userDto for Id=%s not exists", userDto.getId());
        }
        return userManager.addUser(userDto);
    }

    public Optional<UserDto> findUserByUserName(String userName) {
        Objects.nonNull(userName);
        Optional<UserDto> userDto = userManager.getByUserName(userName);
        return userDto;
    }

    public List<UserDto> fuzzyFindUserByUserName(String userName) {
        if (userName == null) {
            userName = "";
        }
        List<UserDto> userDtoList = userManager.fuzzyGetByUserName(userName);
        return userDtoList;
    }

}
