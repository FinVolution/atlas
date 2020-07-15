package com.ppdai.atlas.service;

import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.dto.AppDto;
import com.ppdai.atlas.dto.AppDtoPlus;
import com.ppdai.atlas.dto.AppQuotaDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.query.AppManagerQuery;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by qiankai02 on 2017/11/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlasApplication.class) // 注入appService 这类bean
@Transactional
@Rollback(value = false)
public class AppServiceTest {

    @Autowired
    private AppService appService;

    @Autowired
    private AppQuotaService appQuotaService;

    @Test
    public void testGetOneById() throws Exception {
        AppDtoPlus appDto = TestHelper.getTestObject("service/AppDto.json", AppDtoPlus.class);
        //save
        appDto = appService.addApp(appDto);
        Assert.assertNotNull(appDto);
    }

    @Test
    public void testUpdateOne() throws Exception {
        AppDtoPlus appDto = TestHelper.getTestObject("service/AppDto.json", AppDtoPlus.class);
        //save
        appDto = appService.addApp(appDto);
        appDto.setName("update");
        //update
        AppDtoPlus updateApp = appService.updateApp(appDto);
        Assert.assertEquals("update", updateApp.getName());
    }

    @Test
    public void testGetAll() throws Exception {
        List<AppDtoPlus> appDtos = TestHelper.getTestList("service/AppDtoArray.json", AppDtoPlus.class);
        appDtos.forEach(m -> appService.addApp(m));
        List<AppDto> allApps = appService.getAllApps(null);
        Assert.assertEquals(3, allApps.size());
        Assert.assertEquals("app1", allApps.get(0).getName());
        Assert.assertEquals("v1", allApps.get(0).getExtensions().get("c1"));
        Assert.assertEquals("v3", allApps.get(1).getExtensions().get("c3"));
        //test org not null
        List<AppDto> orgApps = appService.getAllApps(2L);
        Assert.assertEquals(1, orgApps.size());
        Assert.assertEquals("app2", orgApps.get(0).getName());
    }

    @Test
    public void testGetByPage() throws Exception {
        List<AppDtoPlus> appDtos = TestHelper.getTestList("service/AppDtoArray.json", AppDtoPlus.class);
        appDtos.forEach(m -> appService.addApp(m));
        PageDto<AppDto> rolesByPage = appService.getAppsByPage(0, 2);
        Assert.assertEquals(2, rolesByPage.getContent().size());
        Assert.assertEquals("app1", rolesByPage.getContent().get(0).getName());
        //second page
        rolesByPage = appService.getAppsByPage(1, 2);
        Assert.assertEquals(1, rolesByPage.getContent().size());
        Assert.assertEquals("app3", rolesByPage.getContent().get(0).getName());
    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testAllocateAppquota() {

        String appId = "10009001";
        String dev = "dev";
        String fat = "fat";
        String specName = "C-2C4G";

        AppQuotaDto quota = null;
        AppQuotaDto quota2 = null;

        try {
            quota = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(appId, dev, specName);
            quota2 = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(appId, fat, specName);
        } catch (Exception e) {
            Assert.assertNull(quota);
            Assert.assertNull(quota2);
        }

        Optional<AppDto> app = appService.getAppByAppId(appId);
        appService.allocateDefaultAppQuota(app.get());

        quota = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(appId, dev, specName);
        quota2 = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(appId, fat, specName);

        Assert.assertNotNull(quota);
        Assert.assertNotNull(quota2);

        Assert.assertEquals(quota.getNumber().longValue(), 2L);
        Assert.assertEquals(quota2.getNumber().longValue(), 2L);

    }

    @Test
    public void testFindAppByUserName() {
        String username = "qiankai02";
        List<AppDtoPlus> apps = appService.getAppsByUserName(username);
        Assert.assertNotNull(apps);
        System.out.println(apps.size());
    }

    @Test
    public void updateAppManagers() {
        String appId = "1000002614";
        String developNames = "";
        String testNames = "";

        AppManagerQuery appManagerQuery = new AppManagerQuery();
        appManagerQuery.setAppId(appId);
        appManagerQuery.setDevelopers(developNames);
        appManagerQuery.setTesters(testNames);

        appService.updateAppManagers(appManagerQuery);
        appService.getAppPlusByAppId(appId);

    }

}
