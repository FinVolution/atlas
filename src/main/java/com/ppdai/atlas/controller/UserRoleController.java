package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.RoleDto;
import com.ppdai.atlas.dto.UserRoleDto;
import com.ppdai.atlas.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/web/userroles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping(value = "/{userRoleId}")
    public Response<UserRoleDto> getUserRoleById(@PathVariable("userRoleId") Long id) {
        Optional<UserRoleDto> userRoleById = userRoleService.getUserRoleById(id);
        return Response.success(userRoleById.orElse(null));
    }

    @GetMapping(value = "/all")
    public Response<List<UserRoleDto>> getAllUsers() {
        List<UserRoleDto> userRoleDtos = userRoleService.getAllUserRoles();
        return Response.success(userRoleDtos);
    }

    @GetMapping(value = "/page")
    public Response<PageDto<UserRoleDto>> getUserRoleByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<UserRoleDto> userRolesByPage = userRoleService.getUserRolesByPage(page, size);
        return Response.success(userRolesByPage);
    }

    @PostMapping(value = "/{userWorkNumber}")
    public Response<String> addUserRole(@PathVariable String userWorkNumber, @RequestBody List<Long> roleIds) {
        userRoleService.addUserRole(userWorkNumber, roleIds);
        return Response.success("add UserRole success");
    }

    @GetMapping(value = "/roles/{userWorkNumber}")
    public Response<List<RoleDto>> getRolesByUserId(@PathVariable String userWorkNumber) {
        List<RoleDto> rolesByUserId = userRoleService.getUserRolesByUserWorkNumber(userWorkNumber);
        return Response.success(rolesByUserId);
    }

    @GetMapping(value = "/name/{userName}")
    public Response<List<String>> getRolesByUserName(@PathVariable String userName) {
        List<String> rolesByUserId = userRoleService.getUserRolesByUserName(userName);
        return Response.success(rolesByUserId);
    }

    @PutMapping(value = "")
    public Response<UserRoleDto> updateUserRole(@RequestBody UserRoleDto userRoleDto) {
        UserRoleDto userRole = userRoleService.updateUserRole(userRoleDto);
        return Response.success(userRole);
    }

    @DeleteMapping(value = "/{userRoleId}")
    public Response<String> deleteUserRole(@PathVariable("userRoleId") Long id) {
        userRoleService.removeUserRoleById(id);
        return Response.success("delete userRole success");
    }

}
