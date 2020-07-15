package com.ppdai.atlas.entity;

import com.ppdai.atlas.entity.converter.EnvUrl;
import com.ppdai.atlas.entity.converter.ListToJsonStringConverter;
import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Map;


@Data
@Entity
@Table(name = "app", schema = "atlas")
public class AppEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "appid", nullable = false)
    private String appId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column(name = "tests")
    private String tests;

    @Column(name = "developers")
    private String developers;

    @Column(name="env_urls")
    @Convert(converter = ListToJsonStringConverter.class)
    private List<EnvUrl> envUrls;

    @Column(name = "extensions")
    @Convert(converter = MapToStringConverter.class)
    private Map<String, Object> extensions;

    @Column(name = "description")
    private String description;

    @Column(name = "zone_type")
    private String zoneType;

    @Column(name = "enable_ha", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean enableHa;
}
