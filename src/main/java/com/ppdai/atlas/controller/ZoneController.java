package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.ZoneDto;
import com.ppdai.atlas.service.ZoneService;
//import com.ppdai.authority.annotation.AuthorityAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/web/zones")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @GetMapping(value = "/all")
    public Response<List<ZoneDto>> getAllZones() {
        List<ZoneDto> allZones = zoneService.getAllZones();
        return Response.success(allZones);
    }

    @GetMapping(value = "/page")
    public Response<PageDto> getZonesInPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<ZoneDto> zoneInPage = zoneService.getZoneInPage(page, size);
        return Response.success(zoneInPage);
    }

    @GetMapping(value = "/condition")
    public Response<PageDto> searchZonesByEnvAndName(@RequestParam Integer page, @RequestParam Integer size,
                                                     @RequestParam String envName, @RequestParam String zoneName) {
        PageDto<ZoneDto> cloudInPage = zoneService.searchZoneByEnvAndName(page, size, envName, zoneName);
        return Response.success(cloudInPage);
    }

    @GetMapping(value = "/{zoneId}")
    public Response<ZoneDto> getZoneById(@PathVariable("zoneId") Long id) {
        Optional<ZoneDto> zoneDto = zoneService.getZoneById(id);
        return Response.success(zoneDto.orElse(null));
    }

    @PostMapping(value = "")
    public Response<ZoneDto> createZone(@RequestBody ZoneDto zoneDto) {
        zoneDto = zoneService.addOrUpdateZone(zoneDto);
        return Response.success(zoneDto);
    }

    @PutMapping(value = "")
    public Response<ZoneDto> updateZone(@RequestBody ZoneDto zoneDto) {
        zoneDto = zoneService.addOrUpdateZone(zoneDto);
        return Response.success(zoneDto);
    }

    @DeleteMapping(value = "/{zoneId}")
    public Response<String> deleteZone(@PathVariable("zoneId") Long id) {
        zoneService.deleteZone(id);
        return Response.success("cloud delete success");
    }

    @GetMapping(value = "/getByEnv")
    public Response<List<ZoneDto>> getByEnv(@RequestParam String env) {
        List<ZoneDto> zoneDtos = zoneService.getZonesByEnv(env);
        return Response.success(zoneDtos);
    }
}
