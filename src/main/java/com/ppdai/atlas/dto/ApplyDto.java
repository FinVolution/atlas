package com.ppdai.atlas.dto;

import lombok.Data;

@Data
public class ApplyDto {
    private Long id;
    private String type;
    private String status;
    private String request;
    private String result;
    private String applyUser;
    private String applyDepartment;
    private String opUser;
    private Long applyTime;
}
