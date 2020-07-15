package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Date;


@Data
public class BaseDto {
    protected boolean isActive = true;
    protected Date insertTime;
    protected Date updateTime;
    protected String insertBy;
    protected String updateBy;
}
