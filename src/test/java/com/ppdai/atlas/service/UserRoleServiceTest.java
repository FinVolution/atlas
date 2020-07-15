package com.ppdai.atlas.service;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.UserRoleDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Created by qiankai02 on 2017/11/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlasApplication.class)
@Transactional
@Rollback
public class UserRoleServiceTest {

    @Autowired
    private UserRoleService userRoleService;

    @Test
    public void testAddOneById() throws Exception {
        UserRoleDto userRoleDto = TestHelper.getTestObject("service/UserRoleDto.json", UserRoleDto.class);
        //save
        userRoleDto = userRoleService.addUserRole(userRoleDto);
        Assert.assertNotNull(userRoleDto);
        Assert.assertEquals(1, userRoleDto.getRoleId().longValue());
        Assert.assertEquals("v1", userRoleDto.getExtensions().get("c1"));
        Assert.assertEquals("v2", userRoleDto.getExtensions().get("c2"));
    }

    @Test
    public void testUpdateOne() throws Exception {
        UserRoleDto userRoleDto = TestHelper.getTestObject("service/UserRoleDto.json", UserRoleDto.class);
        //save
        userRoleDto = userRoleService.addUserRole(userRoleDto);
        userRoleDto.setUserWorkNumber("009061");
        //update
        UserRoleDto newUserRole = userRoleService.updateUserRole(userRoleDto);
        Assert.assertEquals("009061", newUserRole.getUserWorkNumber());
    }

    @Test
    public void testGetAll() throws Exception {
        List<UserRoleDto> userRoleDtos = TestHelper.getTestList("service/UserRoleDtoArray.json", UserRoleDto.class);
        //userRoleDtos.forEach(m -> userRoleService.addUserRole(m));
        //userRoleService.addUserRole(userRoleDtos);
        List<UserRoleDto> allRoles = userRoleService.getAllUserRoles();
        Assert.assertEquals(3, allRoles.size());
        Assert.assertEquals("009061", allRoles.get(0).getUserWorkNumber());
    }

    @Test
    public void testGetByPage() throws Exception {
        List<UserRoleDto> userDtos = TestHelper.getTestList("service/UserRoleDtoArray.json", UserRoleDto.class);
//        userDtos.forEach(m -> userRoleService.addUserRole(m));
        //userRoleService.addUserRole(userDtos);
        PageDto<UserRoleDto> rolesByPage = userRoleService.getUserRolesByPage(0, 2);
        Assert.assertEquals(2, rolesByPage.getContent().size());
        Assert.assertEquals(1, rolesByPage.getContent().get(0).getUserWorkNumber());
        //second page
        rolesByPage = userRoleService.getUserRolesByPage(1, 2);
        Assert.assertEquals(1, rolesByPage.getContent().size());
        Assert.assertEquals(3, rolesByPage.getContent().get(0).getUserWorkNumber());
    }

    @Test
    public void testDelete() throws Exception {
        UserRoleDto userRoleDto = TestHelper.getTestObject("service/UserRoleDto.json", UserRoleDto.class);
        userRoleDto = userRoleService.addUserRole(userRoleDto);
        userRoleService.removeUserRoleById(userRoleDto.getId());
        userRoleDto = userRoleService.getUserRoleById(userRoleDto.getId()).orElse(null);
        Assert.assertNull(userRoleDto);
    }

}
