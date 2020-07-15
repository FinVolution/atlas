package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Map;


@Data
public class SpecTypeDto extends BaseDto {

    private Long id;

    private String name;

    private Float cpu;

    private Float memory;

    private Float disk;

    private String description;

    private Map extensions;

}
