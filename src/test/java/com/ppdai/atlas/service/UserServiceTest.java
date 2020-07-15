package com.ppdai.atlas.service;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.OrgDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.RoleDto;
import com.ppdai.atlas.dto.UserDto;
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
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by qiankai02 on 2017/11/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlasApplication.class)
@Transactional
@Rollback
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Test
    public void testGetOneById() throws Exception {
        UserDto userDto = TestHelper.getTestObject("service/UserDto.json", UserDto.class);
        //reset orgId to user
        resetOrgIdForUser(Collections.singletonList(userDto));
        userDto = userService.addUser(userDto);
        UserDto newUser = userService.getUserById(userDto.getId()).orElse(null);
        List<RoleDto> roles = userDto.getRoles();
        Assert.assertNotNull(newUser);
        Assert.assertEquals("user1", newUser.getRealName());
        Assert.assertEquals("v1", newUser.getExtensions().get("c1"));
        Assert.assertEquals("v2", newUser.getExtensions().get("c2"));
        //test inner orgDto
        Assert.assertEquals("org1", newUser.getOrgDto().getName());
        //add roles
        List<RoleDto> roleDtos = TestHelper.getTestList("service/RoleDtoArray.json", RoleDto.class);
        roleDtos = roleDtos.stream().map((m -> roleService.addRole(m))).collect(Collectors.toList());
        //test inner roles
    }

    @Test
    public void testUpdateOne() throws Exception {
        UserDto userDto = TestHelper.getTestObject("service/UserDto.json", UserDto.class);
        resetOrgIdForUser(Collections.singletonList(userDto));
        //save
        userDto = userService.addUser(userDto);
        userDto.setRealName("update");
        //update
        UserDto roleDto1 = userService.updateUser(userDto);
        Assert.assertEquals("update", roleDto1.getRealName());
    }

    @Test
    public void testGetAll() throws Exception {
        List<UserDto> userDtos = TestHelper.getTestList("service/UserDtoArray.json", UserDto.class);
        resetOrgIdForUser(userDtos);
        userDtos.forEach(m -> userService.addUser(m));
        List<UserDto> allRoles = userService.getAllUsers();
        Assert.assertEquals(3, allRoles.size());
        Assert.assertEquals("user1", allRoles.get(0).getRealName());
    }

    @Test
    public void testGetByPage() throws Exception {
        List<UserDto> userDtos = TestHelper.getTestList("service/UserDtoArray.json", UserDto.class);
        resetOrgIdForUser(userDtos);
        userDtos.forEach(m -> userService.addUser(m));
        PageDto<UserDto> rolesByPage = userService.getUsersByPage(0, 2);
        Assert.assertEquals(2, rolesByPage.getContent().size());
        Assert.assertEquals("user1", rolesByPage.getContent().get(0).getRealName());

        //second page
        rolesByPage = userService.getUsersByPage(1, 2);
        Assert.assertEquals(1, rolesByPage.getContent().size());
        Assert.assertEquals("user3", rolesByPage.getContent().get(0).getRealName());
    }

    @Test
    public void testDelete() throws Exception {
        UserDto userDto = TestHelper.getTestObject("service/UserDto.json", UserDto.class);
        resetOrgIdForUser(Collections.singletonList(userDto));
        userDto = userService.addUser(userDto);
        userService.removeUserById(userDto.getId());
        userDto = userService.getUserById(userDto.getId()).orElse(null);
        Assert.assertNull(userDto);
    }

    @Test
    public void testFindUserByUserName() {
        Optional<UserDto> userDto = userService.findUserByUserName("lizhiming");
        Assert.assertEquals("lizhiming", userDto.get().getUserName());
    }

    //reset orgId
    private void resetOrgIdForUser(List<UserDto> userDtos) {
        userDtos.forEach(m -> {
            OrgDto orgDto = orgService.addOrg(m.getOrgDto());
            //m.setOrgId(orgDto.getId());
        });
    }

}
