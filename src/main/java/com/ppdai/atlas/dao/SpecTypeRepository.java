package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.SpecTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecTypeRepository extends BaseJpaRepository<SpecTypeEntity, Long> {

    @Override
    @Query("select a from SpecTypeEntity a where a.isActive=true order by a.id")
    List<SpecTypeEntity> findAll();

    @Query("select a from SpecTypeEntity a where a.isActive=true and id=?1")
    SpecTypeEntity findOneById(Long id);

    @Query("select a from SpecTypeEntity a where a.isActive=true order by a.id")
    Page<SpecTypeEntity> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update SpecTypeEntity a set a.isActive=false where a.id=?1")
    void removeOneEntityById(Long id);

    @Query("select a from SpecTypeEntity a where a.isActive=true and a.name=?1")
    SpecTypeEntity findOneByName(String name);

    Page<SpecTypeEntity> findAll(Specification<SpecTypeEntity> specification, Pageable pageable);

}
