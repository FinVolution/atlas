package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.QuotaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface QuotaRepository extends BaseJpaRepository<QuotaEntity, Long> {
    @Override
    @Query("select a from QuotaEntity a where a.isActive=true order by a.id")
    List<QuotaEntity> findAll();

    @Query("select a from QuotaEntity a where a.isActive=true and a.id=?1")
    QuotaEntity findOneById(Long id);

    @Query("select a from QuotaEntity a where a.isActive=true and a.orgId=?1 and a.envId=?2")
    QuotaEntity findOneByOrgEnv(Long orgId, Long envId);

    @Query("select a from QuotaEntity a where a.isActive=true order by a.id")
    @Override
    Page<QuotaEntity> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update QuotaEntity a set a.isActive=false where a.id=?1")
    void removeOneEntityById(Long id);

    Page<QuotaEntity> findAll(Specification<QuotaEntity> specification, Pageable pageRequest);

}
