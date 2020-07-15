package com.ppdai.atlas.entity;

import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@Table(name = "env", schema = "atlas")
public class EnvEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "consul", nullable = false)
    private String consul;

    @Column(name = "nginx")
    private String nginx;

    @Column(name = "dns")
    private String dns;

    @Column(name = "dockeryard")
    private String dockeryard;

    @Column(name = "extensions")
    @Convert(converter = MapToStringConverter.class)
    public Map<String, Object> extensions;

    @Column(name = "description")
    private String description;
}
