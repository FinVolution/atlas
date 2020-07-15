package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.EnvEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnvRepository extends BaseJpaRepository<EnvEntity, Long> {

    @Override
    @Query("SELECT a FROM EnvEntity a WHERE a.isActive=true order by a.id")
    List<EnvEntity> findAll();

    @Override
    @Query("SELECT a From EnvEntity a WHERE a.isActive=true order by a.id")
    Page<EnvEntity> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE EnvEntity a SET a.isActive=false WHERE a.id=?1")
    int removeEnvEntity(Long id);

    @Query("SELECT a FROM EnvEntity a WHERE a.isActive=true and a.id=?1")
    EnvEntity findOneById(Long id);

    @Query("select a from EnvEntity a where a.isActive=true and a.name=?1")
    EnvEntity findOneByName(String name);

    @Query("SELECT a from EnvEntity a where a.name=?1 and a.isActive=?2")
    EnvEntity findOneByNameAndActive(String envName, boolean isActive);

    Page<EnvEntity> findAll(Specification spec, Pageable page);

}
