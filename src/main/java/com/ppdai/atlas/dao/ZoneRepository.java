package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.ZoneEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ZoneRepository extends BaseJpaRepository<ZoneEntity, Long>  {

    @Query("SELECT a FROM ZoneEntity a where a.isActive=true order by a.id")
    @Override
    List<ZoneEntity> findAll();

    @Query("SELECT a FROM ZoneEntity a where a.isActive=true and a.envName=?1")
    List<ZoneEntity> findByEnv(String env);

    @Override
    @Query("SELECT a FROM ZoneEntity a where a.isActive=true order by a.id")
    Page<ZoneEntity> findAll(Pageable pageable);

    @Query("SELECT a FROM ZoneEntity a where a.isActive=true and a.envName LIKE CONCAT('%',:envName,'%') and a.name LIKE CONCAT('%',:zoneName,'%') order by a.id")
    Page<ZoneEntity> findByEnvAndName(Pageable pageable, @Param("envName") String envName, @Param("zoneName") String zoneName);

    @Query("SELECT a FROM ZoneEntity a where a.isActive=true AND a.id=?1")
    ZoneEntity findOneZoneById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE ZoneEntity a SET a.isActive=false WHERE a.id=?1")
    int removeZoneById(Long id);
}
