package com.ppdai.atlas.job;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.manager.LockManager;
import com.ppdai.atlas.service.LdapService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by yinzuolong on 2017/11/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlasApplication.class)
public class LdapUserJobTest {


    @Autowired
    private LockManager lockManager;
    @Autowired
    private LdapService ldapService;

    @Test
    public void name() throws Exception {
        LdapUserJob job = new LdapUserJob();
        job.setLockManager(lockManager);
        job.setLdapService(ldapService);

        job.synchronizeUser();

    }
}
