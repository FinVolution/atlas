package com.ppdai.atlas.dao;


import com.ppdai.atlas.entity.ApplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplyRepository extends JpaRepository<ApplyEntity, Long>, JpaSpecificationExecutor<ApplyEntity> {
}
