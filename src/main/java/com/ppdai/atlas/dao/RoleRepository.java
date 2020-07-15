package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends BaseJpaRepository<RoleEntity, Long> {

    @Override
    @Query("select a from RoleEntity a where a.isActive=true order by a.id")
    List<RoleEntity> findAll();

    @Query("select a from RoleEntity a where a.isActive=true and id=?1")
    RoleEntity findOneById(Long id);

    @Query("select a from RoleEntity a where a.isActive=true order by a.id")
    Page<RoleEntity> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update RoleEntity a set a.isActive=false where a.id=?1")
    void removeOneEntityById(Long id);

    @Query("select a from RoleEntity a where a.isActive=true and a.name=?1")
    RoleEntity findOneByName(String roleName);

    Page<RoleEntity> findAll(Specification<RoleEntity> specification, Pageable pageable);

}
