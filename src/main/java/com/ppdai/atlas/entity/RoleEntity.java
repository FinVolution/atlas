package com.ppdai.atlas.entity;

import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@Table(name = "role", schema = "atlas")
public class RoleEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "extensions")
    @Convert(converter = MapToStringConverter.class)
    public Map<String, Object> extensions;

    @Column(name = "description")
    private String description;
}
