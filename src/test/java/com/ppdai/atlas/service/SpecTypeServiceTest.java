package com.ppdai.atlas.service;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.SpecTypeDto;
import com.ppdai.atlas.dto.UserDto;
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
public class SpecTypeServiceTest {

    @Autowired
    private SpecTypeService specTypeService;

    @Test
    public void testGetOneById() throws Exception {
        SpecTypeDto specTypeDto = TestHelper.getTestObject("service/SpecTypeDto.json", SpecTypeDto.class);
        //save
        specTypeDto = specTypeService.addSpecType(specTypeDto);
        //get
        SpecTypeDto newSpecType = specTypeService.getSpecTypeById(specTypeDto.getId()).orElse(null);
        Assert.assertNotNull(newSpecType);
        Assert.assertEquals("specType1", newSpecType.getName());
        Assert.assertEquals("v1", newSpecType.getExtensions().get("c1"));
        Assert.assertEquals("v2", newSpecType.getExtensions().get("c2"));
    }

    @Test
    public void testUpdateOne() throws Exception {
        SpecTypeDto specTypeDto = TestHelper.getTestObject("service/SpecTypeDto.json", SpecTypeDto.class);
        //save
        specTypeDto = specTypeService.addSpecType(specTypeDto);
        specTypeDto.setName("update");
        //update
        SpecTypeDto updateSpecType = specTypeService.updateSpecType(specTypeDto);
        Assert.assertEquals("update", updateSpecType.getName());
    }

    @Test
    public void testGetAll() throws Exception {
        List<SpecTypeDto> specTypeDtos = TestHelper.getTestList("service/SpecTypeDtoArray.json", SpecTypeDto.class);
        specTypeDtos.forEach(m -> specTypeService.addSpecType(m));
        List<SpecTypeDto> allSpecTypes = specTypeService.getAllSpecTypes();
        Assert.assertEquals(3, allSpecTypes.size());
        Assert.assertEquals("specType1", allSpecTypes.get(0).getName());
    }

    @Test
    public void testGetByPage() throws Exception {
        List<SpecTypeDto> userDtos = TestHelper.getTestList("service/SpecTypeDtoArray.json", SpecTypeDto.class);
        userDtos.forEach(m -> specTypeService.addSpecType(m));
        PageDto<SpecTypeDto> specTypesByPage = specTypeService.getSpecTypesByPage(0, 2);
        Assert.assertEquals(2, specTypesByPage.getContent().size());
        Assert.assertEquals("specType1", specTypesByPage.getContent().get(0).getName());

        //second page
        specTypesByPage = specTypeService.getSpecTypesByPage(1, 2);
        Assert.assertEquals(1, specTypesByPage.getContent().size());
        Assert.assertEquals("specType3", specTypesByPage.getContent().get(0).getName());
    }

    @Test
    public void testDelete() throws Exception {
        SpecTypeDto specTypeDto = TestHelper.getTestObject("service/SpecTypeDto.json", SpecTypeDto.class);
        specTypeDto = specTypeService.addSpecType(specTypeDto);
        specTypeService.removeSpecTypeById(specTypeDto.getId());
        specTypeDto = specTypeService.getSpecTypeById(specTypeDto.getId()).orElse(null);
        Assert.assertNull(specTypeDto);
    }

}
