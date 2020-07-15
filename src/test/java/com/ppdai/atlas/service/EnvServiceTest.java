package com.ppdai.atlas.service;

import com.alibaba.fastjson.JSON;
import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.EnvDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.query.EnvQuery;
import lombok.extern.slf4j.Slf4j;
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
 * Created by qiankai02 on 2017/11/11.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlasApplication.class)
@Transactional
@Rollback()
public class EnvServiceTest {

    @Autowired
    private EnvService envService;

    @Test
    public void testAddOneEnv() throws Exception {
        EnvDto envDto = TestHelper.getTestObject("service/EnvDto.json", EnvDto.class);
        //add env
        EnvDto newEnv = envService.addEnv(envDto);
        Assert.assertNotNull(newEnv);
        log.info(JSON.toJSONString(newEnv));
        Assert.assertEquals("testEnv1", newEnv.getName());
        Assert.assertEquals("v1", newEnv.getExtensions().get("c1"));
        Assert.assertEquals("v2", newEnv.getExtensions().get("c2"));

    }

    @Test
    public void testGetAllEnvs() throws Exception {
        List<EnvDto> envDtos = TestHelper.getTestList("service/EnvDtoArray.json", EnvDto.class);
        envDtos.forEach(m -> envService.addEnv(m));
        List<EnvDto> allEnvs = envService.getAllEnvs();
        Assert.assertEquals(3, allEnvs.size());
        Assert.assertEquals("testEnv1", allEnvs.get(0).getName());
        Assert.assertEquals("testEnv3", allEnvs.get(2).getName());

    }

    @Test
    public void testGetEnvByPage() throws Exception {
        List<EnvDto> envDtos = TestHelper.getTestList("service/EnvDtoArray.json", EnvDto.class);
        envDtos.forEach(m -> envService.addEnv(m));
        //get first page
        PageDto<EnvDto> envsByPage = envService.getEnvsByPage(0, 2);
        Assert.assertEquals(2, envsByPage.getContent().size());
        Assert.assertEquals("testEnv1", envsByPage.getContent().get(0).getName());

        //get second page
        envsByPage = envService.getEnvsByPage(1, 2);
        Assert.assertEquals(1, envsByPage.getContent().size());
        Assert.assertEquals("testEnv3", envsByPage.getContent().get(0).getName());
    }

    @Test
    public void testDeleteEnv() throws Exception {
        EnvDto envDto = TestHelper.getTestObject("service/EnvDto.json", EnvDto.class);
        EnvDto newEnv = envService.addEnv(envDto);
        log.info(newEnv.getId().toString());
        Assert.assertNotNull(newEnv);
        envService.removeEnv(newEnv.getId());
        EnvDto envById = envService.getEnvById(newEnv.getId()).orElse(null);
        Assert.assertNull(envById);
    }

    @Test
    public void testUpdateEnv() throws Exception {
        EnvDto envDto = TestHelper.getTestObject("service/EnvDto.json", EnvDto.class);
        envDto = envService.addEnv(envDto);
        envDto.setName("updateName");
        EnvDto newDto = envService.updateEnv(envDto);
        Assert.assertEquals("updateName", newDto.getName());
    }
}
