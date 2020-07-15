package com.ppdai.atlas.dto.query;

import lombok.Data;

@Data
public class AppQuotaQuery {
    private String appId;
    private Long envId;
}
