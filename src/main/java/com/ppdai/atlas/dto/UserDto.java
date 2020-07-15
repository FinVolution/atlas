package com.ppdai.atlas.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
public class UserDto extends BaseDto {

    private Long id;

    private String workNumber;

    private String realName;

    private String userName;

    private Long orgId;

    private OrgDto orgDto;

    //store the roles one user belongs to
    private List<RoleDto> roles;

    private String email;

    private String source;

    protected Date ldapInsertTime;

    protected Date ldapUpdateTime;

    private Map<String, String> extensions;

    private Date lastVisitAt;
}
