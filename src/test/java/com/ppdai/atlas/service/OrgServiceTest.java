package com.ppdai.atlas.service;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.OrgDto;
import com.ppdai.atlas.dto.PageDto;
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
public class OrgServiceTest {

    @Autowired
    private OrgService orgService;

    @Test
    public void testGetOneById() throws Exception {
        OrgDto orgDto = TestHelper.getTestObject("service/OrgDto.json", OrgDto.class);
        //save
        orgDto = orgService.addOrg(orgDto);
        //get
        OrgDto newOrg = orgService.getOrgById(orgDto.getId()).orElse(null);
        Assert.assertNotNull(newOrg);
        Assert.assertEquals("org1", newOrg.getName());
        Assert.assertEquals("v1", newOrg.getExtensions().get("c1"));
        Assert.assertEquals("v2", newOrg.getExtensions().get("c2"));
    }

    @Test
    public void testUpdateOne() throws Exception {
        OrgDto machineDto = TestHelper.getTestObject("service/OrgDto.json", OrgDto.class);
        //save
        machineDto = orgService.addOrg(machineDto);
        machineDto.setName("update");
        //update
        OrgDto orgDto = orgService.updateOrg(machineDto);
        Assert.assertEquals("update", orgDto.getName());
    }

    @Test
    public void testGetAll() throws Exception {
        List<OrgDto> orgDtos = TestHelper.getTestList("service/OrgDtoArray.json", OrgDto.class);
        orgDtos.forEach(m -> orgService.addOrg(m));
        List<OrgDto> allOrgs = orgService.getAllOrgs();
        Assert.assertEquals(3, allOrgs.size());
        Assert.assertEquals("org1", allOrgs.get(0).getName());
    }

    @Test
    public void testGetByPage() throws Exception {
        List<OrgDto> orgDtos = TestHelper.getTestList("service/OrgDtoArray.json", OrgDto.class);
        orgDtos.forEach(m -> orgService.addOrg(m));
        PageDto<OrgDto> orgsByPage = orgService.getOrgsByPage(0, 2);
        Assert.assertEquals(2, orgsByPage.getContent().size());
        Assert.assertEquals("org1", orgsByPage.getContent().get(0).getName());

        //second page
        orgsByPage = orgService.getOrgsByPage(1, 2);
        Assert.assertEquals(1, orgsByPage.getContent().size());
        Assert.assertEquals("org3", orgsByPage.getContent().get(0).getName());
    }
}
