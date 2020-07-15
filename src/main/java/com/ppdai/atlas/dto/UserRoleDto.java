package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Map;


@Data
public class UserRoleDto extends BaseDto {

    private Long id;

    private String userWorkNumber;

    private Long roleId;

    private String description;

    public Map extensions;
}
