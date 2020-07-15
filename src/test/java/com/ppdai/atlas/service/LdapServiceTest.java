package com.ppdai.atlas.service;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * Created by yinzuolong on 2017/11/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlasApplication.class)
public class LdapServiceTest {

    @Autowired
    private LdapService ldapService;
    @Autowired
    private UserService userService;

    @Test
    public void testSynchronizeUserByName() throws Exception {
        Optional<UserDto> userDtoOptional = ldapService.synchronizeUser("luoshougeng");
        System.out.println(userDtoOptional.orElse(null));
    }

    @Test
    public void testSynchronizeUserAll() throws Exception {
        ldapService.synchronizeUser();
        List<UserDto> all = userService.getAllUsers();
        System.out.println(all);
    }
}
