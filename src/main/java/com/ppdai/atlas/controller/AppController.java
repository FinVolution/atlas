package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.AppDto;
import com.ppdai.atlas.dto.AppDtoPlus;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.query.AppManagerQuery;
import com.ppdai.atlas.dto.query.AppQuery;
import com.ppdai.atlas.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/web/apps")
public class AppController {

    private final AppService appService;

    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
    }

    //使用:stargate
    @GetMapping(value = "/app/{appId}")
    public Response<AppDto> getAppByAppId(@PathVariable("appId") String appId) {
        Optional<AppDto> appDto = appService.getAppByAppId(appId);
        return Response.success(appDto.orElse(null));
    }

    //使用:atlas
    @GetMapping(value = "/page/plus")
    public Response<PageDto<AppDtoPlus>> getAppPlusByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<AppDtoPlus> appDtoPage = appService.getAppPlusByPage(page, size);
        return Response.success(appDtoPage);
    }

    //使用:appsync
    @GetMapping(value = "/all")
    public Response<List<AppDto>> getAllApps(@RequestParam(required = false) Long orgId) {
        List<AppDto> allApps = appService.getAllApps(orgId);
        return Response.success(allApps);
    }

    //使用:atlas
    @GetMapping(value = "/condition/plus")
    public Response<PageDto<AppDtoPlus>> getAppPlusByConditions(@RequestParam Integer page, @RequestParam Integer size,
                                                         AppQuery appQuery) {
        PageDto<AppDtoPlus> apps = appService.getAppPlusByConditions(page, size, appQuery);
        return Response.success(apps);
    }

    //使用:atlas
    @PostMapping(value = "")
    public Response<AppDtoPlus> addApp(@RequestBody AppDtoPlus appDto) {
        appDto = appService.addApp(appDto);
        return Response.success(appDto);
    }

    //使用:atlas
    @PutMapping(value = "")
    public Response<AppDtoPlus> updateApp(@RequestBody AppDtoPlus appDto) {
        AppDtoPlus updatedApp = appService.updateApp(appDto);
        return Response.success(updatedApp);
    }

    //使用:atlas
    @DeleteMapping(value = "/{appId}")
    public Response<String> deleteApp(@PathVariable("appId") Long id) {
        appService.removeAppById(id);
        return Response.success("delete query success");
    }

    //使用:atlas
    @GetMapping(value = "/fuzzy/appname")
    public Response<List<AppDto>> fuzzySearchAppsByAppName(@RequestParam(value = "appName") String appName) {
        List<AppDto> appDtos = appService.fuzzyGetAppsByAppName(appName);
        return Response.success(appDtos);
    }

    //使用:atlas
    @GetMapping(value = "/appname")
    public Response<List<AppDto>> findAppByAppName( @RequestParam String appName) {
        List<AppDto> appDtos = appService.findAppByAppName(appName);
        return Response.success(appDtos);
    }

    //使用:stargate
    @GetMapping(value = "/username")
    public Response<List<AppDtoPlus>> findAppsByUserName(@RequestParam String username) {
        return Response.success(appService.getAppsByUserName(username));
    }

    //使用:stargate
    @GetMapping(value = "/all/appplus")
    public Response<List<AppDtoPlus>> getAllAppPluss(@RequestParam(required = false) Long orgId) {
        List<AppDtoPlus> allApps = appService.getAllAppPlus(orgId);
        return Response.success(allApps);
    }

    //使用:stargate
    @GetMapping(value = "/appmanager")
    public Response<Boolean> updateAppManager(@RequestBody AppManagerQuery managerQuery) {
        Boolean result = appService.updateAppManagers(managerQuery);
        return Response.success(result);
    }

}
