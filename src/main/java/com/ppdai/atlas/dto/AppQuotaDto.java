package com.ppdai.atlas.dto;

import com.ppdai.atlas.entity.BaseEntity;
import com.ppdai.atlas.entity.converter.MapToStringConverter;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;


@Data
public class AppQuotaDto extends BaseEntity {

    private Long id;

    private String appId;

    private String appName;

    private Long envId;

    private Long orgId;

    private String orgName;

    private String envName;

    private Long spectypeId;

    private String spectypeName;

    private Long number;
}
