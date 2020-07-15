package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.OrgDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.query.OrgQuery;
import com.ppdai.atlas.service.OrgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/web/orgs")
public class OrgController {

    private final OrgService orgService;

    @Autowired
    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @GetMapping(value = "/{orgId}")
    public Response<OrgDto> getOrgById(@PathVariable("orgId") Long id) {
        Optional<OrgDto> orgDto = orgService.getOrgById(id);
        return Response.success(orgDto.orElse(null));
    }

    @GetMapping(value = "/all")
    public Response<List<OrgDto>> getAllOrgs() {
        List<OrgDto> orgDtos = orgService.getAllOrgs();
        return Response.success(orgDtos);
    }

    @GetMapping(value = "/page")
    public Response<PageDto<OrgDto>> getOrgsByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<OrgDto> orgDtoPageDto = orgService.getOrgsByPage(page, size);
        return Response.success(orgDtoPageDto);
    }

    @GetMapping(value = "/condition")
    public Response<PageDto<OrgDto>> getOrgsByCondition(@RequestParam Integer page, @RequestParam Integer size,
                                                        OrgQuery orgQuery) throws Exception {
        PageDto<OrgDto> orgDtoPageDto = orgService.getOrgsByCondition(page, size, orgQuery);
        return Response.success(orgDtoPageDto);
    }

    @PostMapping(value = "")
    public Response<OrgDto> addOrg(@RequestBody OrgDto orgDto) {
        orgDto = orgService.addOrg(orgDto);
        return Response.success(orgDto);
    }

    @PutMapping(value = "")
    public Response<OrgDto> updateOrg(@RequestBody OrgDto orgDto) {
        OrgDto updateOrg = orgService.updateOrg(orgDto);
        return Response.success(updateOrg);
    }

    @DeleteMapping(value = "/{orgId}")
    public Response<String> deleteOrg(@PathVariable("orgId") Long id) {
        orgService.removeOrgById(id);
        return Response.success("delete org success");
    }

}
