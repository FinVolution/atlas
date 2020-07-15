package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Map;

@Data
public class QuotaDtoPlus extends BaseDto {

    private Long id;

    private Long orgId;

    private String orgName;

    private OrgDto orgDto;

    private Long envId;

    private EnvDto envDto;

    private Long cpu;

    private Long memory;

    private Long disk;

    private Map extensions;

    private String description;
}
