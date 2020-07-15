package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Map;

@Data
public class QuotaDto extends BaseDto {

    private Long id;

    private Long orgId;

    private String orgName;

    private OrgDto orgDto;

    private Long envId;

    private EnvDto envDto;

    private Long specTypeId;

    private SpecTypeDto specTypeDto;

    private Float limit;

    private Float balance;

    private Map extensions;

    private String description;
}
