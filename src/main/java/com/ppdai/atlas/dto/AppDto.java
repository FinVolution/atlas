package com.ppdai.atlas.dto;

import com.ppdai.atlas.entity.converter.EnvUrl;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class AppDto extends BaseDto {
    private Long id;

    private String appId;

    private String name;

    private Long orgId;

    private OrgDto orgDto;

    private String description;

    private String appType;

    private String appLevel;

    private String influenceScope;

    private String healthUrl;

    private String serviceType;

    private List<EnvUrl> envUrls;

    private String userWorkNumber;

    private UserDto userDto;

    private Map<String, Object> extensions;

    private String zoneType;

    private Boolean enableHa;
}
