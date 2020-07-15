package com.ppdai.atlas.manager;

import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dao.RoleRepository;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.RoleDto;
import com.ppdai.atlas.dto.query.RoleQuery;
import com.ppdai.atlas.entity.RoleEntity;
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
public class RoleManager {

    private RoleRepository roleRepository;

    @Autowired
    public RoleManager(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<RoleDto> getRoleById(Long id) {
        RoleEntity roleEntity = roleRepository.findOneById(id);
        return Optional.ofNullable(roleEntity).map(m -> ConvertUtils.convert(m, RoleDto.class));
    }

    @Transactional
    public RoleDto addRole(RoleDto roleDto) {
        RoleEntity roleEntity = ConvertUtils.convert(roleDto, RoleEntity.class);
        roleEntity = roleRepository.save(roleEntity);
        roleRepository.flush();
        roleDto = ConvertUtils.convert(roleEntity, RoleDto.class);
        return roleDto;
    }

    public List<RoleDto> getAllRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        return ConvertUtils.convert(roleEntities, RoleDto.class);
    }

    public PageDto<RoleDto> getRoleByPage(int page, int size) {
        PageRequest pageRequest = new PageRequest(page, size);
        Page<RoleEntity> orgEntities = roleRepository.findAll(pageRequest);
        return ConvertUtils.convertPage(orgEntities, RoleDto.class);
    }

    @Transactional
    public void removeRoleById(Long id) {
        roleRepository.removeOneEntityById(id);
    }

    public PageDto<RoleDto> getRolesByCondition(RoleQuery roleQuery, Pageable pageRequest) {
        RoleQuery query = (roleQuery == null) ? new RoleQuery() : roleQuery;
        Page<RoleEntity> roleEntities = roleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Optional.ofNullable(query.getName()).ifPresent(o -> predicates.add(criteriaBuilder.like(root.get("name").as(String.class), "%" + query.getName() + "%")));
            predicates.add(criteriaBuilder.equal(root.get("isActive").as(Boolean.class), true));
            Predicate[] arrayPredicates = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(arrayPredicates));
        }, pageRequest);
        PageDto<RoleDto> roleDtoPageDto = ConvertUtils.convertPage(roleEntities, RoleDto.class);

        return roleDtoPageDto;
    }

}
