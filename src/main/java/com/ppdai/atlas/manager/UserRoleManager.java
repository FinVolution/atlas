package com.ppdai.atlas.manager;

import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.RoleRepository;
import com.ppdai.atlas.dao.UserRepository;
import com.ppdai.atlas.dao.UserRoleRepository;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.RoleDto;
import com.ppdai.atlas.dto.UserRoleDto;
import com.ppdai.atlas.entity.RoleEntity;
import com.ppdai.atlas.entity.UserEntity;
import com.ppdai.atlas.entity.UserRoleEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserRoleManager {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    //by id
    public Optional<UserRoleDto> getUserRoleById(Long id) {
        UserRoleEntity userRoleEntity = userRoleRepository.findOneById(id);
        return Optional.ofNullable(userRoleEntity).map(m -> ConvertUtils.convert(m, UserRoleDto.class));
    }

    @Transactional
    public List<UserRoleDto> addUserRole(String userWorkNumber, List<Long> roleIds) {
        //remove old roles for the userId
        userRoleRepository.removeEntitiesByUserWorkNumber(userWorkNumber);
        //if roleIds is empty, return
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        //then add new roles
        List<UserRoleDto> userRoleDtos = roleIds.stream().map(m -> {
            UserRoleDto userRoleDto = new UserRoleDto();
            userRoleDto.setRoleId(m);
            userRoleDto.setUserWorkNumber(userWorkNumber);
            return userRoleDto;
        }).collect(Collectors.toList());

        List<UserRoleEntity> userRoleEntities = userRoleDtos.stream().map(m -> ConvertUtils.convert(m, UserRoleEntity.class)).collect(Collectors.toList());
        userRoleEntities = userRoleEntities.stream().map(m -> userRoleRepository.save(m)).collect(Collectors.toList());
        return ConvertUtils.convert(userRoleEntities, UserRoleDto.class);
    }

    //专为负责人添加角色
    @Transactional
    public void addUserRolesToAppManagers(String userWorkNumber, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }
        //查看user-role是否存在
        for (Long id : roleIds) {
            UserRoleEntity userRoleEntity = userRoleRepository.findOneByWorkNumberAndRoleId(userWorkNumber, id);
            if (userRoleEntity == null) {
                UserRoleEntity temp = new UserRoleEntity();
                temp.setUserWorkNumber(userWorkNumber);
                temp.setRoleId(id);
                userRoleRepository.save(temp);
            }
        }

    }

    @Transactional
    public UserRoleDto addUserRole(String userWorkNumber, Long roleId) {
        return addUserRole(userWorkNumber, Collections.singletonList(roleId)).get(0);
    }

    @Transactional
    public UserRoleDto addUserRole(UserRoleDto userRoleDto) {
        UserRoleEntity userRoleEntity = ConvertUtils.convert(userRoleDto, UserRoleEntity.class);
        userRoleEntity = userRoleRepository.save(userRoleEntity);
        return ConvertUtils.convert(userRoleEntity, UserRoleDto.class);
    }


    public List<UserRoleDto> getAllUserRoless() {
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findAll();
        return ConvertUtils.convert(userRoleEntities, UserRoleDto.class);
    }

    public PageDto<UserRoleDto> getUserRoleByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<UserRoleEntity> userRoleEntities = userRoleRepository.findAll(pageRequest);
        return ConvertUtils.convertPage(userRoleEntities, UserRoleDto.class);
    }

    @Transactional
    public void removeUserRoleById(Long id) {
        userRoleRepository.removeOneEntityById(id);
    }

    //get roles by useWorkNumber
    public List<RoleDto> getRolesByUserWorkNumber(String useWorkNumber) {
        List<UserRoleEntity> userRolesByUserId = userRoleRepository.findUserRolesByUserWorkNumber(useWorkNumber);
        List<RoleDto> roleDtos = userRolesByUserId.stream().map(m -> {
            RoleEntity roleEntity = roleRepository.findOneById(m.getRoleId());
            return ConvertUtils.convert(roleEntity, RoleDto.class);
        }).collect(Collectors.toList());
        return roleDtos;
    }

    //get roles by userId
    public List<String> getRolesByUserName(String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        List<UserRoleEntity> userRolesByUserId = userRoleRepository.findUserRolesByUserWorkNumber(userEntity.getWorkNumber());
        List<String> roleStr = userRolesByUserId.stream().map(m -> {
            RoleEntity roleEntity = roleRepository.findOneById(m.getRoleId());
            return roleEntity.getName();
        }).collect(Collectors.toList());
        return roleStr;
    }

}
