package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.SpecTypeDto;
import com.ppdai.atlas.dto.query.SpecTypeQuery;
import com.ppdai.atlas.service.SpecTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * Created by qiankai02 on 2017/11/13.
 */
@Slf4j
@RestController
@RequestMapping("/web/specTypes")
public class SpecTypeController {

    private final SpecTypeService specTypeService;

    @Autowired
    public SpecTypeController(SpecTypeService specTypeService) {
        this.specTypeService = specTypeService;
    }

    @GetMapping(value = "/{specTypeId}")
    public Response<SpecTypeDto> getSpecTypeById(@PathVariable("specTypeId") Long id) {
        Optional<SpecTypeDto> specTypeById = specTypeService.getSpecTypeById(id);
        return Response.success(specTypeById.orElse(null));
    }

    @GetMapping(value = "/all")
    public Response<List<SpecTypeDto>> getAllSpecTypes() {
        List<SpecTypeDto> allSpecTypes = specTypeService.getAllSpecTypes();
        return Response.success(allSpecTypes);
    }

    @GetMapping(value = "/page")
    public Response<PageDto<SpecTypeDto>> getSpecTypeByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<SpecTypeDto> specTypesByPage = specTypeService.getSpecTypesByPage(page, size);
        return Response.success(specTypesByPage);
    }

    @GetMapping(value = "/condition")
    public Response<PageDto<SpecTypeDto>> getSpecTypeByCondition(@RequestParam Integer page, @RequestParam Integer size,
                                                                 SpecTypeQuery specTypeQuery) {
        PageDto<SpecTypeDto> specTypesByPage = specTypeService.getSpecTypesByCondition(page, size, specTypeQuery);
        return Response.success(specTypesByPage);
    }

    @PostMapping(value = "")
    public Response<SpecTypeDto> addSpecType(@RequestBody SpecTypeDto specTypeDto) {
        specTypeDto = specTypeService.addSpecType(specTypeDto);
        return Response.success(specTypeDto);
    }

    @PutMapping(value = "")
    public Response<SpecTypeDto> updateSpecType(@RequestBody SpecTypeDto specTypeDto) {
        SpecTypeDto updateSpecType = specTypeService.updateSpecType(specTypeDto);
        return Response.success(updateSpecType);
    }

    @DeleteMapping(value = "/{specTypeId}")
    public Response<String> deleteSpecType(@PathVariable("specTypeId") Long id) {
        specTypeService.removeSpecTypeById(id);
        return Response.success("delete specTypeDto success");
    }

}
