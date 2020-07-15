package com.ppdai.atlas.service;

import com.ppdai.atlas.dao.AuditLogRepository;
import com.ppdai.atlas.entity.AuditLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public void addAuditLog(AuditLogEntity auditLogEntity) {
        auditLogRepository.save(auditLogEntity);
    }

}
