package com.ppdai.atlas.manager;

import com.ppdai.atlas.dao.LockRepository;
import com.ppdai.atlas.entity.LockEntity;
import com.ppdai.atlas.utils.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Component
public class LockManager {

    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private Environment env;

    @Transactional
    public boolean tryLockBefore(String name, int beforeSecond) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, -1 * beforeSecond);
        LockEntity lockEntity = getLockEntity(name);
        int count = lockRepository.tryLockBefore(name, lockEntity.getVersion(), getPossessor(), c.getTime());
        return count == 1;
    }

    @Transactional
    public void release(String name) {
        lockRepository.releaseLock(name, getPossessor());
    }

    private String getPossessor() {
        String port = env.getProperty("server.port");
        return IPUtil.getLocalIP() + ":" + port;
    }

    private LockEntity getLockEntity(String name) {
        LockEntity lockEntity = lockRepository.findByName(name);
        if (lockEntity == null) {
            lockEntity = new LockEntity();
            lockEntity.setName(name);
            lockEntity.setVersion(0L);
            lockEntity = lockRepository.save(lockEntity);
        }
        return lockEntity;
    }

}
