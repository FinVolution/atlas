package com.ppdai.atlas.dto;

import com.ppdai.atlas.entity.converter.EnvUrl;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 与 AppDto 相同，返回是多负责人
 */
@Data
public class AppDtoPlus extends BaseDto {
    private Long id;

    private String appId;

    private String name;

    private Long orgId;

    private OrgDto orgDto;

    private String description;

    private List<EnvUrl> envUrls;

    private String developers;

    private String tests;

    private List<UserDto> userDtos;

    //主要给前台使用
    private List<UserDto> testUserDtos;

    private Map<String, Object> extensions;

    private String zoneType;

    private Boolean enableHa;
}
