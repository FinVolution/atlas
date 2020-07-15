package com.ppdai.atlas.service;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.RoleDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by qiankai02 on 2017/11/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlasApplication.class)
@Transactional
@Rollback
public class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Test
    public void testGetOneById() throws Exception {
        RoleDto roleDto = TestHelper.getTestObject("service/RoleDto.json", RoleDto.class);
        //save
        roleDto = roleService.addRole(roleDto);
        //get
        RoleDto newRole = roleService.getRoleById(roleDto.getId()).orElse(null);
        Assert.assertNotNull(newRole);
        Assert.assertEquals("role1", newRole.getName());
        Assert.assertEquals("v1", newRole.getExtensions().get("c1"));
        Assert.assertEquals("v2", newRole.getExtensions().get("c2"));
    }

    @Test
    public void testUpdateOne() throws Exception {
        RoleDto roleDto = TestHelper.getTestObject("service/RoleDto.json", RoleDto.class);
        //save
        roleDto = roleService.addRole(roleDto);
        roleDto.setName("update");
        //update
        RoleDto roleDto1 = roleService.updateRole(roleDto);
        Assert.assertEquals("update", roleDto1.getName());
    }

    @Test
    public void testGetAll() throws Exception {
        List<RoleDto> roleDtos = TestHelper.getTestList("service/RoleDtoArray.json", RoleDto.class);
        roleDtos.forEach(m -> roleService.addRole(m));
        List<RoleDto> allRoles = roleService.getAllRoles();
        Assert.assertEquals(3, allRoles.size());
        Assert.assertEquals("role1", allRoles.get(0).getName());
    }

    @Test
    public void testGetByPage() throws Exception {
        List<RoleDto> roleDtos = TestHelper.getTestList("service/RoleDtoArray.json", RoleDto.class);
        roleDtos.forEach(m -> roleService.addRole(m));
        PageDto<RoleDto> rolesByPage = roleService.getRolesByPage(0, 2);
        Assert.assertEquals(2, rolesByPage.getContent().size());
        Assert.assertEquals("role1", rolesByPage.getContent().get(0).getName());

        //second page
        rolesByPage = roleService.getRolesByPage(1, 2);
        Assert.assertEquals(1, rolesByPage.getContent().size());
        Assert.assertEquals("role3", rolesByPage.getContent().get(0).getName());
    }

    @Test
    public void testDelete() throws Exception {
        RoleDto roleDto = TestHelper.getTestObject("service/RoleDto.json", RoleDto.class);
        roleDto = roleService.addRole(roleDto);
        roleService.removeRoleById(roleDto.getId());
        roleDto = roleService.getRoleById(roleDto.getId()).orElse(null);
        Assert.assertNull(roleDto);
    }

}
