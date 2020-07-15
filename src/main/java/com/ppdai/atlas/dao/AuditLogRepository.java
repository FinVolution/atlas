package com.ppdai.atlas.dao;

import com.ppdai.atlas.entity.AuditLogEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuditLogRepository extends CrudRepository<AuditLogEntity, Long> {
}
