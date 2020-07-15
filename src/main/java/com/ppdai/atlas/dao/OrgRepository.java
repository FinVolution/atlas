package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.OrgEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgRepository extends BaseJpaRepository<OrgEntity, Long> {

    @Override
    @Query("select a from OrgEntity a where a.isActive=true order by a.id")
    List<OrgEntity> findAll();

    @Query("select a from OrgEntity a where a.isActive=true and id=?1")
    OrgEntity findOneById(Long id);

    @Query("select a from OrgEntity a where a.isActive=true order by a.id")
    @Override
    Page<OrgEntity> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update OrgEntity a set a.isActive=false where a.id=?1")
    void removeOneEntityById(Long id);

    Page<OrgEntity> findAll(Specification<OrgEntity> specification, Pageable pageable);

    @Query("select a from OrgEntity a where a.orgCode=?1 and a.isActive=true")
    OrgEntity findOrgByOrgCode(String orgCode);
}
