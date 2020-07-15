package com.ppdai.atlas.dao;


import com.ppdai.atlas.entity.UserExtEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;

public interface UserExtRepository extends BaseJpaRepository<UserExtEntity, Long> {

    @Query("select a from UserExtEntity a where a.userEntity.workNumber=?1 and a.isActive=true")
    UserExtEntity findUserExtByUserWorkNumber(String userWorkNumber);

    Page<UserExtEntity> findAll(Specification<UserExtEntity> specification, Pageable pageable);

}
