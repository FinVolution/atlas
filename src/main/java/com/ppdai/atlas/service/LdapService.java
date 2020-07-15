package com.ppdai.atlas.service;

import com.ppdai.atlas.common.utils.ConvertUtils;
import com.ppdai.atlas.dto.UserDto;
import com.ppdai.atlas.manager.LdapManager;
import com.ppdai.atlas.manager.UserManager;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
@ConfigurationProperties("ldapService")
public class LdapService {
    @Autowired
    private LdapManager ldapManager;
    @Autowired
    private UserManager userManager;
    @Setter
    private String source = "ldap";

    public Optional<UserDto> synchronizeUser(String userName) throws Exception {
        Optional<UserDto> userDtoOptional = ldapManager.queryActivedByName(userName);
        userDtoOptional.ifPresent(this::accept);

        userDtoOptional = ldapManager.queryDisabledByName(userName);
        userDtoOptional.ifPresent(this::accept);
        return userDtoOptional;
    }

    /**
     * 增量同步用户
     */
    public void synchronizeUser() throws Exception {
        //同步 在职的用户
        Optional<UserDto> userDtoOptional = userManager.getLastUpdateUserFromSource(source);
        Date date = userDtoOptional.map(UserDto::getLdapUpdateTime).orElse(getInitTime());
        ldapManager.queryActiveUserAfter(date, this::accept);

        //同步 删除的用户
        userDtoOptional = userManager.getLastDeleteUserFromSource(source);
        date = userDtoOptional.map(UserDto::getLdapUpdateTime).orElse(getInitTime());
        ldapManager.queryDisabledUserAfter(date, this::accept);
    }

    private void accept(UserDto ldapUser) {
        // 比对用户，存在就更新，不存在就插入
        Optional<UserDto> localUserDto = userManager.getByUserName(ldapUser.getUserName());
        ldapUser.setSource("ldap");
        if (localUserDto.isPresent()) {
            ConvertUtils.convert(ldapUser, localUserDto.get(),"id");
            userManager.updateUser(localUserDto.get());
        } else {
            userManager.addUser(ldapUser);
        }
    }

    private Date getInitTime() {
        Calendar c = Calendar.getInstance();
        c.set(1980, 1, 1);
        return c.getTime();
    }
}