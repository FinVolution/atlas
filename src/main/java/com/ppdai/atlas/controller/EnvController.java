package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.EnvDto;
import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.query.EnvQuery;
import com.ppdai.atlas.service.EnvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/web/envs")
public class EnvController {

    @Autowired
    private EnvService envService;

    @GetMapping(value = "/all")
    public Response<List<EnvDto>> getAllEnvs() {
        List<EnvDto> allEnvs = envService.getAllEnvs();
        return Response.success(allEnvs);
    }

    @GetMapping(value = "/page")
    public Response<PageDto<EnvDto>> getEnvsInPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<EnvDto> envInPage = envService.getEnvsByPage(page, size);
        return Response.success(envInPage);
    }

    @GetMapping(value = "/{envId}")
    public Response<EnvDto> getEnvById(@PathVariable("envId") Long id) {
        Optional<EnvDto> envDto = envService.getEnvById(id);
        return Response.success(envDto.orElse(null));
    }

    @PostMapping(value = "")
    public Response<EnvDto> createEnv(@RequestBody EnvDto envDto) {
        envDto = envService.addEnv(envDto);
        return Response.success(envDto);
    }

    @PutMapping(value = "")
    public Response<EnvDto> updateEnv(@RequestBody EnvDto envDto) {
        envDto = envService.updateEnv(envDto);
        return Response.success(envDto);
    }

    @DeleteMapping(value = "/{envId}")
    public Response<String> deleteEnv(@PathVariable("envId") Long id) {
        envService.removeEnv(id);
        return Response.success("env delete success");
    }
}
