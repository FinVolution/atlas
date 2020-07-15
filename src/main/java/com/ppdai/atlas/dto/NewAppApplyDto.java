package com.ppdai.atlas.dto;

import lombok.Data;

@Data
public class NewAppApplyDto {
    private String appId;
    private String appName;
    private String department;
    private Integer instanceCount;
    private String instanceSpec;
    private String zoneType;
}
