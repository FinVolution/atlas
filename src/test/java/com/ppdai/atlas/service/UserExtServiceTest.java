package com.ppdai.atlas.service;

import com.ppdai.atlas.dto.UserExtDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
//@Rollback(value = false)
public class UserExtServiceTest {

    @Autowired
    UserExtService userExtService;

    @Before
    public void init() {

    }

    @Test
    public void testAddUserExt() {
        UserExtDto userExtDto = new UserExtDto();

        userExtDto.setUserWorkNumber("000542");
        userExtDto.setOrgId(33L);

        UserExtDto newUserExtDto = userExtService.addUserExt(userExtDto);

        //test add
        Assert.assertEquals(newUserExtDto.getUserWorkNumber(), "000542");
        Assert.assertEquals(newUserExtDto.getOrgId().longValue(), 33L);

        userExtDto = userExtService.findUserExtByWorkNumber("000542");
        //test select
        Assert.assertNotNull(userExtDto);

        userExtService.removeUserExtByWorkNumber("000542");
        userExtDto = userExtService.findUserExtByWorkNumber("000542");
        //test remove
        Assert.assertNull(userExtDto);

    }

    @Test
    public void testRemoveUserExt() {
        userExtService.removeUserExtByWorkNumber("009061");
    }

    @Test
    public void testFindUserExt() {
        UserExtDto userExtDto = userExtService.findUserExtByWorkNumber("009061");
        Assert.assertNotNull(userExtDto);
        Assert.assertEquals(33, userExtDto.getOrgId().longValue());
    }


}
