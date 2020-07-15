package com.ppdai.atlas.job;

import com.ppdai.atlas.manager.LockManager;
import com.ppdai.atlas.service.LdapService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@ConfigurationProperties("job.ldap-user")
@ConditionalOnProperty(name = "job.ldap-user.enable", havingValue = "true")
@Component
@Slf4j
public class LdapUserJob {

    @Autowired
    @Setter
    private LockManager lockManager;
    @Autowired
    @Setter
    private LdapService ldapService;
    @Setter
    private int fixedRate = 300000;
    @Setter
    private String lockName = "synchronizeUser";

    @Scheduled(initialDelayString = "${job.ldap-user.initialDelay}", fixedRateString = "${job.ldap-user.fixedRate}")
    public void synchronizeUser() {

        //抢锁
        boolean locked = lockManager.tryLockBefore(lockName, fixedRate / 1000);
        log.info("locked: " + locked);
        if (locked) {
            try {
                log.info("start synchronizing user");
                //同步用户
                ldapService.synchronizeUser();
                //执行成功，设定success状态
                //metric 埋点记录正确数
            } catch (Exception e) {
                //执行失败，设定失败原因
                log.error("error synchronize from ldap.", e);
            } finally {
                lockManager.release(lockName);
            }
        }
    }
}
