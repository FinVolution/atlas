package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.QuotaDto;
import com.ppdai.atlas.dto.QuotaDtoPlus;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.query.QuotaQuery;
import com.ppdai.atlas.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/web/quotas")
public class QuotaController {

    private final QuotaService quotaService;

    @Autowired
    public QuotaController(QuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @GetMapping(value = "/{quotaId}")
    public Response<QuotaDtoPlus> getQuotaById(@PathVariable("quotaId") Long id) {
        Optional<QuotaDtoPlus> userDto = quotaService.getQuotaById(id);
        return Response.success(userDto.orElse(null));
    }

    /**
     * 用以取代 getQuotaByEnvAndOrg() 接口, 返回 QuotaDtoPlus 格式的数据
     * @param orgCode
     * @param envName
     * @param specName
     * @return
     */
    @GetMapping(value = "/queryquota")
    public Response<QuotaDtoPlus> getQuotaByOrgEnvSpec(@RequestParam String orgCode, @RequestParam String envName, @RequestParam String specName) {
        QuotaDtoPlus quotaDtoPlus = quotaService.getQuotaByOrgEnvSpecPlus(orgCode, envName, specName);
        return Response.success(quotaDtoPlus);
    }

    @GetMapping(value = "/all")
    public Response<List<QuotaDtoPlus>> getAllQuotas() {
        List<QuotaDtoPlus> allQuotas = quotaService.getAllQuotas();
        return Response.success(allQuotas);
    }

    @GetMapping(value = "/page")
    public Response<PageDto<QuotaDtoPlus>> getQuotasByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<QuotaDtoPlus> quotaByPage = quotaService.getQuotasByPage(page, size);
        return Response.success(quotaByPage);
    }

    @GetMapping(value = "/condition")
    public Response<PageDto<QuotaDtoPlus>> getQuotasByCondition(@RequestParam Integer page, @RequestParam Integer size, QuotaQuery quotaQuery) {
        PageDto<QuotaDtoPlus> quotaByPage = quotaService.getQuotasByCondition(quotaQuery, page, size);
        return Response.success(quotaByPage);
    }

    @PostMapping(value = "")
    public Response<QuotaDtoPlus> addQuota(@RequestBody QuotaDtoPlus quotaDtoPlus) {
        quotaDtoPlus = quotaService.addQuota(quotaDtoPlus);
        return Response.success(quotaDtoPlus);
    }

    @PutMapping(value = "")
    public Response<QuotaDtoPlus> updateQuota(@RequestBody QuotaDtoPlus quotaDtoPlus) {
        QuotaDtoPlus updateQuota = quotaService.updateQuota(quotaDtoPlus);
        return Response.success(updateQuota);
    }

    @DeleteMapping(value = "/{quotaId}")
    public Response<String> deleteQuota(@PathVariable("quotaId") Long id) {
        quotaService.removeQuotaById(id);
        return Response.success("delete quota success");
    }

}
