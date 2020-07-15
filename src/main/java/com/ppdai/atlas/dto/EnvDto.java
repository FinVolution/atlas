package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Map;

@Data
public class EnvDto extends BaseDto {

    private Long id;

    private String name;

    private String consul;

    private String nginx;

    private String dns;

    private String dockeryard;

    private String zones;

    private Map extensions;

    private String description;
}
