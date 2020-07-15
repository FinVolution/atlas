package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.LockEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;


public interface LockRepository extends BaseJpaRepository<LockEntity, Long> {

    @Query("SELECT a FROM LockEntity a WHERE a.isActive=true and a.name=?1")
    LockEntity findByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE LockEntity a SET a.version=a.version+1, a.possessor=?3  WHERE a.name=?1 and a.version=?2 and a.updateTime < ?4")
    int tryLockBefore(String name, long version, String possessor, Date date);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE LockEntity a SET a.possessor='' WHERE a.name=?1 and a.possessor=?2")
    void releaseLock(String name, String possessor);
}
