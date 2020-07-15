package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Map;


@Data
public class RoleDto extends BaseDto {

    private Long id;

    private String name;

    private Map extensions;

    private String description;
}
