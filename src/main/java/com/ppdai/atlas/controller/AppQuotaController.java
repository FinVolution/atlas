package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.AppQuotaDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.query.AppQuotaQuery;
import com.ppdai.atlas.service.AppQuotaService;
import com.ppdai.authority.annotation.AuthorityAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/web/appquotas")
public class AppQuotaController {

    private final AppQuotaService appQuotaService;

    @Autowired
    public AppQuotaController(AppQuotaService appQuotaService) {
        this.appQuotaService = appQuotaService;
    }

    @GetMapping(value = "/all")
    public Response<List<AppQuotaDto>> getAllQuotas() {
        List<AppQuotaDto> allQuotas = appQuotaService.getAllAppQuotas();
        return Response.success(allQuotas);
    }

    @GetMapping(value = "/page")
    public Response<PageDto<AppQuotaDto>> getQuotasByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<AppQuotaDto> quotaByPage = appQuotaService.getAppQuotasByPage(page, size);
        return Response.success(quotaByPage);
    }

    @PostMapping(value = "")
    @AuthorityAnnotation(name = "admin")
    public Response<String> addQuota(@RequestBody AppQuotaDto appQuotaDto) {
        appQuotaService.addAppQuota(appQuotaDto);
        return Response.success("添加应用配额成功");
    }

    @PutMapping(value = "")
    @AuthorityAnnotation(name = "admin")
    public Response<String> updateQuota(@RequestBody AppQuotaDto appQuotaDto) {
        appQuotaService.updateAppQuota(appQuotaDto);
        return Response.success("更新应用配额成功");
    }

    @DeleteMapping(value = "/{appQuotaId}")
    @AuthorityAnnotation(name = "admin")
    public Response<String> deleteAppQuota(@PathVariable("appQuotaId") Long id) {
        appQuotaService.removeAppQuotaById(id);
        return Response.success("删除应用配额成功");
    }

    @GetMapping(value = "/condition")
    public Response<PageDto<AppQuotaDto>> findAppQuotaByCondition(@RequestParam Integer page, @RequestParam Integer size, AppQuotaQuery appQuotaQuery) {
        PageDto<AppQuotaDto> appQuotaDtoPageDto = appQuotaService.findAppQuotaByCondition(appQuotaQuery, page, size);
        return Response.success(appQuotaDtoPageDto);
    }

    /**
     * 获取app,env下所有申请的配额
     *
     * @param appId
     * @param envName
     * @return
     */
    @GetMapping(value = "/appenv")
    public Response<List<AppQuotaDto>> findAllAppQuotaByAppAndEnv(String appId, String envName) {
        List<AppQuotaDto> appQuotas = appQuotaService.findAppQuotasByAppIdAndEnvName(appId, envName);
        return Response.success(appQuotas);
    }

    @GetMapping(value = "/appenvspec")
    public Response<AppQuotaDto> getAppQuotaByAppIdEnvNameAndSpecName(@RequestParam String appId, @RequestParam String envName, @RequestParam String spectypeName) {
        AppQuotaDto appQuotaDto = appQuotaService.findAppQuotaByAppIdEnvNameAndSpecName(appId, envName, spectypeName);
        return Response.success(appQuotaDto);
    }
}
