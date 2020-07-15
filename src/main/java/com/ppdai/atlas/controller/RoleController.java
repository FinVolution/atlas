package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.RoleDto;
import com.ppdai.atlas.dto.query.RoleQuery;
import com.ppdai.atlas.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/web/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/{roleId}")
    public Response<RoleDto> getRoleById(@PathVariable("roleId") Long id) {
        Optional<RoleDto> roleDto = roleService.getRoleById(id);
        return Response.success(roleDto.orElse(null));
    }

    @GetMapping(value = "/all")
    public Response<List<RoleDto>> getAllRoles() {
        List<RoleDto> roleDtos = roleService.getAllRoles();
        return Response.success(roleDtos);
    }

    @GetMapping(value = "/page")
    public Response<PageDto<RoleDto>> getRoleByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<RoleDto> roleDtoByPage = roleService.getRolesByPage(page, size);
        return Response.success(roleDtoByPage);
    }

    @GetMapping(value = "/condition")
    public Response<PageDto<RoleDto>> getRoleByCondition(@RequestParam Integer page, @RequestParam Integer size,
                                                         RoleQuery roleQuery) {
        PageDto<RoleDto> roleDtoByPage = roleService.getRolesByCondition(page, size, roleQuery);
        return Response.success(roleDtoByPage);
    }

    @PostMapping(value = "")
    public Response<RoleDto> addRole(@RequestBody RoleDto roleDto) {
        roleDto = roleService.addRole(roleDto);
        return Response.success(roleDto);
    }

    @PutMapping(value = "")
    public Response<RoleDto> updateRole(@RequestBody RoleDto roleDto) {
        RoleDto updateRole = roleService.updateRole(roleDto);
        return Response.success(updateRole);
    }

    @DeleteMapping(value = "/{roleId}")
    public Response<String> deleteRole(@PathVariable("roleId") Long id) {
        roleService.removeRoleById(id);
        return Response.success("delete role success");
    }

}
