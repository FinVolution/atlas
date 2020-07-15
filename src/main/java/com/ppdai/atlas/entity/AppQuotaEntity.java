package com.ppdai.atlas.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "app_quota", schema = "atlas")
public class AppQuotaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "appid", nullable = false)
    private String appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column(name = "env_id", nullable = false)
    private Long envId;

    @Column(name = "spectype_id", nullable = false)
    private Long spectypeId;

    @Column(name = "number", nullable = false)
    private Long number;
}
