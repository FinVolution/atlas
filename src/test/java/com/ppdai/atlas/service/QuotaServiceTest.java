package com.ppdai.atlas.service;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.QuotaDtoPlus;
import com.ppdai.atlas.dto.query.QuotaQuery;
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
public class QuotaServiceTest {

    @Autowired
    private QuotaService quotaService;

    @Autowired
    private SpecTypeService specTypeService;

    @Test
    public void testGetOneById() throws Exception {
        QuotaDtoPlus quotaDtoPlus = TestHelper.getTestObject("service/QuotaDto.json", QuotaDtoPlus.class);
        resetSpecTypeIdForQuota(Collections.singletonList(quotaDtoPlus));
        quotaDtoPlus = quotaService.addQuota(quotaDtoPlus);
        QuotaDtoPlus newQuota = quotaService.getQuotaById(quotaDtoPlus.getId()).orElse(null);
        Assert.assertNotNull(newQuota);
        Assert.assertEquals("v1", newQuota.getExtensions().get("c1"));
        Assert.assertEquals("v2", newQuota.getExtensions().get("c2"));
        //test inner specType
        //Assert.assertEquals("specType1", newQuota.getSpecTypeDto().getName());
    }

    @Test
    public void testUpdateOne() throws Exception {
        QuotaDtoPlus quotaDtoPlus = TestHelper.getTestObject("service/QuotaDto.json", QuotaDtoPlus.class);
        resetSpecTypeIdForQuota(Collections.singletonList(quotaDtoPlus));
        //save
        quotaDtoPlus = quotaService.addQuota(quotaDtoPlus);
        //update
        QuotaDtoPlus newQuota = quotaService.updateQuota(quotaDtoPlus);
    }

    @Test
    public void testGetAll() throws Exception {
        List<QuotaDtoPlus> quotaDtoPluses = TestHelper.getTestList("service/QuotaDtoArray.json", QuotaDtoPlus.class);
        resetSpecTypeIdForQuota(quotaDtoPluses);
        quotaDtoPluses.forEach(m -> quotaService.addQuota(m));
        List<QuotaDtoPlus> allQuotas = quotaService.getAllQuotas();
        Assert.assertEquals(3, allQuotas.size());
    }

    @Test
    public void testGetByPage() throws Exception {
        List<QuotaDtoPlus> quotaDtoPluses = TestHelper.getTestList("service/QuotaDtoArray.json", QuotaDtoPlus.class);
        resetSpecTypeIdForQuota(quotaDtoPluses);
        quotaDtoPluses.forEach(m -> quotaService.addQuota(m));
        PageDto<QuotaDtoPlus> quotaByPage = quotaService.getQuotasByPage(0, 2);
        Assert.assertEquals(2, quotaByPage.getContent().size());

        //second page
        quotaByPage = quotaService.getQuotasByPage(1, 2);
        Assert.assertEquals(1, quotaByPage.getContent().size());
    }

    @Test
    public void testDelete() throws Exception {
        QuotaDtoPlus quotaDtoPlus = TestHelper.getTestObject("service/QuotaDto.json", QuotaDtoPlus.class);
        resetSpecTypeIdForQuota(Collections.singletonList(quotaDtoPlus));
        quotaDtoPlus = quotaService.addQuota(quotaDtoPlus);
        quotaService.removeQuotaById(quotaDtoPlus.getId());
        quotaDtoPlus = quotaService.getQuotaById(quotaDtoPlus.getId()).orElse(null);
        Assert.assertNull(quotaDtoPlus);
    }

    @Test
    public void testGetQuotaByCondition() throws Exception {
        QuotaQuery quotaQuery = TestHelper.getTestObject("service/QuotaQueryDto.json", QuotaQuery.class);
        PageDto<QuotaDtoPlus> quotas = quotaService.getQuotasByCondition(quotaQuery, 0, 10);
        Assert.assertEquals(1, quotas.getContent().size());
        //
        quotaQuery.setEnvId(null);
        //quotaQuery.setSpecTypeId(null);
        quotas = quotaService.getQuotasByCondition(quotaQuery, 0, 10);
        Assert.assertEquals(2, quotas.getContent().size());
    }

    private void resetSpecTypeIdForQuota(List<QuotaDtoPlus> quotaDtoPluses) {
        quotaDtoPluses.forEach(m -> {
            //SpecTypeDto specTypeDto = specTypeService.addSpecType(m.getSpecTypeDto());
            //m.setCpu(specTypeDto.getId());
        });
    }

}
