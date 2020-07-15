package com.ppdai.atlas.entity;

import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@Table(name = "org", schema = "atlas")
public class OrgEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "org_code", nullable = false)
    private String orgCode;

    @Column(name = "parent_org_id", nullable = false)
    private Long parentOrgId;

    @Column(name = "user_work_number")
    private String userWorkNumber;

    @Column(name = "extensions")
    @Convert(converter = MapToStringConverter.class)
    public Map<String, Object> extensions;

    @Column(name = "description")
    private String description;
}
