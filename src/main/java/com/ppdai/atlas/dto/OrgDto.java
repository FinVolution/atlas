package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Map;

@Data
public class OrgDto extends BaseDto {

    private Long id;

    private String name;

    private String orgCode;

    private Long parentOrgId;

    //代替 userID
    private String userWorkNumber;

    private UserDto userDto;

    private Map extensions;

    private String description;
}
