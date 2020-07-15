package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.UserRoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends BaseJpaRepository<UserRoleEntity, Long> {

    @Override
    @Query("select a from UserRoleEntity a where a.isActive=true order by a.id")
    List<UserRoleEntity> findAll();

    @Query("select a from UserRoleEntity a where a.isActive=true and id=?1")
    UserRoleEntity findOneById(Long id);

    @Query("select a from UserRoleEntity a where a.isActive=true order by a.id")
    @Override
    Page<UserRoleEntity> findAll(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update UserRoleEntity a set a.isActive=false where a.id=?1")
    void removeOneEntityById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update UserRoleEntity a set a.isActive=false where a.userWorkNumber=?1")
    void removeEntitiesByUserWorkNumber(String workNumber);

    @Query("select a from UserRoleEntity a where a.isActive=true and a.userWorkNumber=?1")
    List<UserRoleEntity> findUserRolesByUserWorkNumber(String userWorkNumber);

    @Query("select a from UserRoleEntity a where a.isActive=true and a.userWorkNumber=?1 and a.roleId=?2")
    UserRoleEntity findOneByWorkNumberAndRoleId(String userWorkNumber, Long roleId);
}
