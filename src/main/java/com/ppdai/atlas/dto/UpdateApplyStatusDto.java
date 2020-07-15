package com.ppdai.atlas.dto;

import lombok.Data;

@Data
public class UpdateApplyStatusDto {
    private Long applyId;
    private String opUser;
    private String result;
    private String status;
}
