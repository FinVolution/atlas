package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.AppQuotaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppQuotaRepository extends BaseJpaRepository<AppQuotaEntity, Long> {

    @Override
    @Query("select a from AppQuotaEntity a where a.isActive=true order by a.id")
    List<AppQuotaEntity> findAll();

    @Query("select a from AppQuotaEntity a where a.isActive=true and a.id=?1")
    AppQuotaEntity findOneById(Long id);

    @Query("select a from AppQuotaEntity a where a.isActive=true and a.appId=?1 and a.envId=?2 and a.spectypeId=?3")
    AppQuotaEntity findOneByAppEnvSpec(String appId, Long envId, Long spectypeId);

    @Query("select a from AppQuotaEntity a where a.isActive=true order by a.id")
    @Override
    Page<AppQuotaEntity> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update AppQuotaEntity a set a.isActive=false where a.id=?1")
    void removeOneEntityById(Long id);

    @Query("select a from AppQuotaEntity  a where a.isActive=true and a.appId=?1 and a.envId=?2")
    List<AppQuotaEntity> findAllByAppIdAndEnvId(String appId, Long envId);

    @Query("select a from AppQuotaEntity a where a.isActive=true and a.appId=?1")
    List<AppQuotaEntity> findAllAppQuotaByAppId(String appId);

    Page<AppQuotaEntity> findAll(Specification<AppQuotaEntity> specification, Pageable pageRequest);

    @Query("select a from AppQuotaEntity  a where a.isActive=true and a.appId=?1 and a.envId=?2 and a.spectypeId=?3")
    AppQuotaEntity findByAppIdEnvIdAndSpecId(String appId, long envId, long specId);

}
