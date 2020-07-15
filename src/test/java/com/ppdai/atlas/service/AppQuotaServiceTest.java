package com.ppdai.atlas.service;

import com.ppdai.atlas.dto.AppQuotaDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
public class AppQuotaServiceTest {

    private String appId;

    private String envName;

    @Autowired
    private AppQuotaService appQuotaService;

    @Before
    public void before() {
        appId = "20180517";
        envName = "fat";
    }


    @Test
    public void testGetAllAppQuotaByAppIdAndEvnName() {
        String appId = "20180517";
        String envName = "uat";

        List<AppQuotaDto> appQuotas = appQuotaService.findAppQuotasByAppIdAndEnvName(appId, envName);
        Assert.assertEquals(2, appQuotas.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAppQuotaByAppIdEnvNameAndSpecName() {
        String appId = "20180517";
        String envName = "uat";
        String specName = "B-1C2G";

        AppQuotaDto appQuotaDto = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(appId, envName, specName);
        Assert.assertNotNull(appQuotaDto);
        Assert.assertEquals(appQuotaDto.getSpectypeName(), specName);

        //异常
        //specName = "B-1C2G";
        //appQuotaDto = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(appId, envName, specName);
        //Assert.assertEquals(appQuotaDto.getEnvName(), envName);

        envName = "prd";
        appQuotaDto = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(appId, envName, specName);
    }

}
