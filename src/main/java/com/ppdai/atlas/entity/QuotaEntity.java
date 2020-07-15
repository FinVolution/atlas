package com.ppdai.atlas.entity;

import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@Table(name = "quota", schema = "atlas")
public class QuotaEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column(name = "org_name", nullable = false)
    private String orgName;

    @Column(name = "env_id", nullable = false)
    private Long envId;

    @Column(name = "cpu", nullable = false)
    private Long cpu;

    @Column(name = "memory", nullable = false)
    private Long memory;

    @Column(name = "disk", nullable = false)
    private Long disk;

    @Column(name = "extensions")
    @Convert(converter = MapToStringConverter.class)
    public Map<String, Object> extensions;

    @Column(name = "description")
    private String description;
}
