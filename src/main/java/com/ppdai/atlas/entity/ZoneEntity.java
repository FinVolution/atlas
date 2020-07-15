package com.ppdai.atlas.entity;

import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@Table(name = "zone", schema = "atlas")
public class ZoneEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "env_id", nullable = false)
    private Long envId;

    @Column(name = "env_name", nullable = false)
    private String envName;

    @Column(name = "k8s", nullable = false)
    private String k8s;

    @Column(name = "k8s_version", nullable = false)
    private String k8sVersion;

    @Column(name = "description")
    private String description;

    @Column(name = "extensions")
    @Convert(converter = MapToStringConverter.class)
    private Map<String, Object> extensions;
}
