package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ZoneDto extends BaseDto {

    private Long id;

    private String name;

    private Long envId;

    private String envName;

    private String k8s;

    private String k8sVersion;

    private Map extensions;

    private String description;
}
