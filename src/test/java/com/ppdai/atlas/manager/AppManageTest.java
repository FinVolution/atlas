package com.ppdai.atlas.manager;

import com.google.common.collect.Lists;
import com.ppdai.atlas.AtlasApplication;
import com.ppdai.atlas.common.exception.AtlasServiceException;
import com.ppdai.atlas.dao.AppRepository;
import com.ppdai.atlas.dto.AppDtoPlus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AtlasApplication.class)
//@Transactional()
@Rollback()
public class AppManageTest {
    @Autowired
    private AppManager appManager;

    @Autowired
    private AppRepository appRepository;

    private AppDtoPlus appDto1;

    private AppDtoPlus appDto2;

    @Before
    public void before() {
        appDto1 = new AppDtoPlus();
        appDto1.setAppId("54455445");
        appDto1.setName("tttt");
        appDto1.setOrgId(33L);
        appDto1.setDevelopers("009061");


        appDto2 = new AppDtoPlus();
        appDto2.setAppId("54455445");
        appDto2.setName("ssss");
        appDto2.setOrgId(33L);
        appDto2.setDevelopers("339061");
    }


    @Test
    public void testAddApp2() {

        appManager.addApp(appDto1);

        try {
            appManager.addApp(appDto2);
        } catch (AtlasServiceException e) {
            Assert.assertEquals("appId=" + appDto2.getAppId() + " already exists", e.getMessage());
        }

        appDto2.setAppId("55555555");
        appDto2.setName("tttt");

        try {
            appManager.addApp(appDto2);
        } catch (AtlasServiceException e) {
            Assert.assertEquals("appName=" + appDto2.getName() + " already exists", e.getMessage());
        }

        appDto2.setName("ssss");
        AppDtoPlus newAppDto = appManager.addApp(appDto2);
        Assert.assertEquals(newAppDto.getName(), "ssss");

    }

    @Test
    public void testUpdateApp2() {
        AppDtoPlus appDto = appManager.addApp(appDto1);

        AppDtoPlus newApp = appManager.updateApp(appDto);
        Assert.assertEquals(newApp.getName(), appDto.getName());

        //插入新名字
        appDto.setName("new-tttt");
        appManager.updateApp(appDto);

        //插入对比数据
        appDto2.setName("ssss");
        appDto2.setAppId("55555555");
        AppDtoPlus app2 = appManager.addApp(appDto2);

        //构造重复名称有效数据
        newApp.setName(appDto2.getName());
        try {
            appManager.updateApp(newApp);
        } catch (Exception e) {
            Assert.assertEquals("appName=" + appDto2.getName() + " already exists", e.getMessage());
        }

        //删除有效数据
        newApp.setName("new-tttt");
        newApp.setActive(false);
        AppDtoPlus invalidApp = appManager.updateApp(newApp);
        Assert.assertFalse(invalidApp.isActive());

        //更新数据使其与无效数据同名
        app2.setName(newApp.getName());
        try {
            appManager.updateApp(app2);
        } catch (Exception e) {
            Assert.assertEquals("appName=" + app2.getName() + " already exists", e.getMessage());
        }
    }

    @Test
    public void testLoadRoleIds() {
        ArrayList<String> devRoleNames = Lists.newArrayList("123");
        ArrayList<String> testRoleNames = Lists.newArrayList("stargate-fat", "", "stargate-uat", "stargate-pro");

        List<Long> devRoleIds = appManager.loadRoleIds(devRoleNames);
        List<Long> testRoleIds = appManager.loadRoleIds(testRoleNames);

        System.out.println(devRoleIds);
        System.out.println(testRoleIds);
    }
}
