package com.ppdai.atlas.entity;

import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@Table(name = "spec_type", schema = "atlas")
public class SpecTypeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cpu", nullable = false)
    private Float cpu;

    @Column(name = "memory", nullable = false)
    private Float memory;

    @Column(name = "disk", nullable = false)
    private Float disk;

    @Column(name = "extensions")
    @Convert(converter = MapToStringConverter.class)
    public Map<String, Object> extensions;

    @Column(name = "description")
    private String description;

}
