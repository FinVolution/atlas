package com.ppdai.atlas.manager;

import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.*;
import com.ppdai.atlas.dto.OrgDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.RoleDto;
import com.ppdai.atlas.dto.UserDto;
import com.ppdai.atlas.dto.query.UserQuery;
import com.ppdai.atlas.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private UserExtRepository userExtRepository;

    //添加 orgDto 和 rolesDto
    private UserDto setOrgAndRolesDtoToUser(UserEntity userEntity) {
        UserDto userDto = ConvertUtils.convert(userEntity, UserDto.class);

        //从user_ext 表读记录
        UserExtEntity userExtEntity = userExtRepository.findUserExtByUserWorkNumber(userEntity.getWorkNumber());
        if (userExtEntity != null) {
            OrgEntity orgEntity = orgRepository.findOneById(userExtEntity.getOrgId());
            userDto.setOrgDto(ConvertUtils.convert(orgEntity, OrgDto.class));
        }
        List<RoleDto> rolesByUserId = getRolesByUserWorkNumber(userEntity.getWorkNumber());
        userDto.setRoles(rolesByUserId);
        return userDto;
    }

    public Optional<UserDto> getUserById(Long id) {
        UserEntity userEntity = userRepository.findOneById(id);
        return Optional.ofNullable(userEntity).map(m -> setOrgAndRolesDtoToUser(m));
    }

    public List<RoleDto> getRolesByUserWorkNumber(String userWorkNumber) {
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findUserRolesByUserWorkNumber(userWorkNumber);
        if (userRoleEntities.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> rolesId = userRoleEntities.stream().map(m -> m.getRoleId()).collect(Collectors.toList());
        //排除无效角色isActive false 或者不存在
        rolesId = rolesId.stream().filter(m -> roleRepository.findOneById(m) != null).collect(Collectors.toList());
        List<RoleEntity> roleEntities = rolesId.stream().map(m -> roleRepository.findOneById(m)).collect(Collectors.toList());
        return ConvertUtils.convert(roleEntities, RoleDto.class);
    }

    public Optional<UserDto> getByUserName(String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        return Optional.ofNullable(userEntity).map(m -> setOrgAndRolesDtoToUser(m));
    }

    public List<UserDto> fuzzyGetByUserName(String userName) {
        //通过page, size 限定糊查询的返回数量
        Pageable pageable = new PageRequest(0, 10);
        Page<UserEntity> userEntityPage = userRepository.fuzzyFindByUserName(userName, pageable);
        List<UserEntity> userEntities = userEntityPage.getContent();
        return userEntities.stream().map(m -> setOrgAndRolesDtoToUser(m)).collect(Collectors.toList());
    }

    public Optional<UserDto> getLastUpdateUserFromSource(String source) {
        UserDto userDto = null;
        Page<UserEntity> result = userRepository.findLdapLastUpdateFromSource(source, new PageRequest(0, 1));
        if (result.getContent().size() >= 1) {
            userDto = ConvertUtils.convert(result.getContent().get(0), UserDto.class);
        }
        return Optional.ofNullable(userDto);
    }

    public Optional<UserDto> getLastDeleteUserFromSource(String source) {
        UserDto userDto = null;
        Page<UserEntity> result = userRepository.findLdapLastDeleteFromSource(source, new PageRequest(0, 1));
        if (result.getContent().size() >= 1) {
            userDto = ConvertUtils.convert(result.getContent().get(0), UserDto.class);
        }
        return Optional.ofNullable(userDto);
    }

    @Transactional
    public UserDto addUser(UserDto userDto) {
        UserEntity userEntity = ConvertUtils.convert(userDto, UserEntity.class);
        userEntity = userRepository.save(userEntity);
        userDto = setOrgAndRolesDtoToUser(userEntity);
        return userDto;
    }

    public UserDto updateUser(UserDto userDto) {
        UserEntity userEntity = ConvertUtils.convert(userDto, UserEntity.class);
        userEntity = userRepository.save(userEntity);
        userDto = ConvertUtils.convert(userEntity, UserDto.class);
        return userDto;
    }

    public List<UserDto> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> userDtos = userEntities.stream().map(m -> setOrgAndRolesDtoToUser(m)).collect(Collectors.toList());
        return userDtos;
    }

    public PageDto<UserDto> getUserByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<UserEntity> userEntities = userRepository.findAll(pageRequest);
        List<UserDto> userDtos = userEntities.getContent().stream().map(m -> setOrgAndRolesDtoToUser(m)).collect(Collectors.toList());
        PageDto<UserDto> userDtoPageDto = ConvertUtils.convertPage(userEntities, UserDto.class);
        userDtoPageDto.setContent(userDtos);
        return userDtoPageDto;
    }

    //只做 user 表内的多条件查询
    public PageDto<UserDto> getUserByCondition(UserQuery userQuery, PageRequest pageRequest) {
        UserQuery query = (userQuery == null) ? new UserQuery() : userQuery;
        List<Predicate> predicates = new ArrayList<>();

        Page<UserEntity> userEntityPage = userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            Optional.ofNullable(query.getRealName()).ifPresent(o -> predicates.add(criteriaBuilder.like(root.get("realName").as(String.class), "%" + query.getRealName() + "%")));
            Optional.ofNullable(query.getUserName()).ifPresent(o -> predicates.add(criteriaBuilder.like(root.get("userName").as(String.class), "%" + query.getUserName() + "%")));

            predicates.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), Boolean.TRUE));
            Predicate[] predicateArray = new Predicate[predicates.size()];

            predicates.toArray(predicateArray);
            return criteriaBuilder.and(predicateArray);
        }, pageRequest);

        List<UserDto> userDtoList = userEntityPage.getContent().stream().map(m -> setOrgAndRolesDtoToUser(m)).collect(Collectors.toList());

        PageDto<UserDto> userDtoPageDto = ConvertUtils.convertPage(userEntityPage, UserDto.class);
        userDtoPageDto.setContent(userDtoList);
        return userDtoPageDto;
    }

    //通过表关联实现 org 及 user 表的多表查询
    public PageDto<UserDto> getUserOrgInfoByCondition(UserQuery userQuery, PageRequest pageRequest) {

        //不涉及组织时直接通过 user 表查看, 无需 join 表关联
        if (userQuery.getOrgCode() == null || ("".equals(userQuery.getOrgCode()))) {
            return getUserByCondition(userQuery, pageRequest);
        }

        OrgEntity orgEntity = orgRepository.findOrgByOrgCode(userQuery.getOrgCode());
        Specification<UserExtEntity> userSpecification = new Specification<UserExtEntity>() {
            @Override
            public Predicate toPredicate(Root<UserExtEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get("orgId").as(Long.class), orgEntity.getId()));

                Join<UserExtEntity, UserEntity> join = root.join(root.getModel().getSingularAttribute("userEntity", UserEntity.class), JoinType.INNER);
                Optional.ofNullable(userQuery.getUserName()).ifPresent(o -> predicates.add(cb.like(join.get("userName").as(String.class), "%" + userQuery.getUserName() + "%")));
                Optional.ofNullable(userQuery.getRealName()).ifPresent(o -> predicates.add(cb.like(join.get("realName").as(String.class), "%" + userQuery.getRealName() + "%")));
                predicates.add(cb.equal(root.get("isActive").as(Boolean.class), Boolean.TRUE));
                Predicate[] predicateArray = new Predicate[predicates.size()];
                predicates.toArray(predicateArray);

                return cb.and(predicateArray);
            }

        };

        Page<UserExtEntity> userExtEntities = userExtRepository.findAll(userSpecification, pageRequest);
        List<UserExtEntity> userExtEntityList = userExtEntities.getContent();
        PageDto<UserDto> userDtoPageDto = new PageDto<>();
        //手动复制 相同属性
        BeanUtils.copyProperties(userExtEntities, userDtoPageDto);
        if (userExtEntityList.size() > 0) {
            List<UserEntity> userEntities = userExtEntityList.stream().map(item -> item.getUserEntity()).collect(Collectors.toList());
            List<UserDto> userDtos = userEntities.stream().map(item -> setOrgAndRolesDtoToUser(item)).collect(Collectors.toList());
            userDtoPageDto.setContent(userDtos);
        }
        return userDtoPageDto;
    }

    @Transactional
    public void removeUserById(Long id) {
        userRepository.removeOneEntityById(id);
    }


}
