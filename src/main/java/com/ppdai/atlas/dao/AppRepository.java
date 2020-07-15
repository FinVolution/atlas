package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.AppEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AppRepository extends BaseJpaRepository<AppEntity, Long> {
    @Override
    @Query("select a from AppEntity a where a.isActive=true order by a.id")
    List<AppEntity> findAll();

    @Query("select a from AppEntity a where a.isActive=true and a.orgId=?1 order by a.id")
    List<AppEntity> findAllByOrgId(Long id);

    @Query("select a from AppEntity a where a.isActive=true and a.appId=?1")
    AppEntity findOneByAppId(String id);

    @Query("select a from AppEntity a where a.appId=?1")
    AppEntity findOneByAppIdNoCareActive(String id);

    @Query("select a from AppEntity a where a.isActive=true and a.id=?1")
    AppEntity findOneByAutoIncreId(Long id);

    @Query("select a from AppEntity a where a.isActive=true and a.name=?1")
    AppEntity findAppByAppName(String appName);

    @Query("select a from AppEntity a where a.name=?1")
    AppEntity findAppByNameNotCareActive(String appName);

    @Query("select a from AppEntity a where a.isActive=true order by a.id")
    @Override
    Page<AppEntity> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update AppEntity a set a.isActive=false where a.id=?1")
    void removeOneEntityById(Long id);

    Page<AppEntity> findAll(Specification specification, Pageable pageable);

    @Query("SELECT a FROM AppEntity a where a.isActive=true and a.name LIKE CONCAT('%',:appName,'%') order by a.id")
    List<AppEntity> fuzzyFindByAppName(@Param("appName") String appName);

    //按负责人和test来查
    @Query("select a from AppEntity a where a.isActive=true and (a.developers LIKE CONCAT('%',:developers,'%') or a.tests LIKE CONCAT('%',:developers,'%')) order by a.id")
    List<AppEntity> findAppsByUserWorkNumber(@Param("developers")String userWorkNumber);
}
