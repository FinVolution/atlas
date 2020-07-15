package com.ppdai.atlas.dto;

import lombok.Data;

@Data
public class UserExtDto extends BaseDto {

    private Long id;

    private String userWorkNumber;

    private Long orgId;

}
