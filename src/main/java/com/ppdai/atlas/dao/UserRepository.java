package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends BaseJpaRepository<UserEntity, Long> {
    @Override
    @Query("select a from UserEntity a where a.isActive=true order by a.id")
    List<UserEntity> findAll();

    @Query("select a from UserEntity a where a.isActive=true and a.id=?1")
    UserEntity findOneById(Long id);

    @Query("select a from UserEntity a where a.isActive=true and a.workNumber=?1")
    List<UserEntity> findByWorkNumber(String workNumber);

    @Query("select a from UserEntity a where a.isActive=true order by a.id")
    @Override
    Page<UserEntity> findAll(Pageable pageable);

    @Query("select a from UserEntity a where a.userName=?1")
    UserEntity findByUserName(String userName);

    @Query("select a from UserEntity a where a.isActive=true and a.workNumber <> '' and a.userName LIKE CONCAT('%',:userName,'%') order by a.id")
    Page<UserEntity> fuzzyFindByUserName(@Param("userName") String userName, Pageable pageable);

    @Query("select a from UserEntity a where a.isActive=true and a.source=?1 order by a.ldapUpdateTime desc")
    Page<UserEntity> findLdapLastUpdateFromSource(String source, Pageable pageable);

    @Query("select a from UserEntity a where a.isActive=false and a.source=?1 order by a.ldapUpdateTime desc")
    Page<UserEntity> findLdapLastDeleteFromSource(String source, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update UserEntity a set a.isActive=false where a.id=?1")
    void removeOneEntityById(Long id);

    Page<UserEntity> findAll(Specification<UserEntity> specification, Pageable pageable);

}
