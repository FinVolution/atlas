package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.PageDto;
import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.UserDto;
import com.ppdai.atlas.dto.query.UserQuery;
import com.ppdai.atlas.service.UserService;
//import com.ppdai.authority.annotation.AuthorityAnnotation;
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
@RequestMapping("/web/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/userid/{userId}")
    public Response<UserDto> getUserById(@PathVariable("userId") Long id) {
        Optional<UserDto> userDto = userService.getUserById(id);
        return Response.success(userDto.orElse(null));
    }

    @GetMapping(value = "/all")
    public Response<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = userService.getAllUsers();
        return Response.success(userDtos);
    }

    @GetMapping(value = "/page")
    public Response<PageDto<UserDto>> getUserByPage(@RequestParam Integer page, @RequestParam Integer size) {
        PageDto<UserDto> usersByPage = userService.getUsersByPage(page, size);
        return Response.success(usersByPage);
    }

    @GetMapping(value = "/condition")
    public Response<PageDto<UserDto>> getUserByCondition(@RequestParam Integer page, @RequestParam Integer size,
                                                         UserQuery userQuery) {
        PageDto<UserDto> users = userService.getUsersByCondition(userQuery, page, size);
        return Response.success(users);
    }

    @PostMapping(value = "")
    public Response<String> addUser(@RequestBody UserDto userDto) {
        userService.addUser(userDto);
        return Response.success("add user success");
    }

    @PutMapping(value = "")
    public Response<UserDto> updateUser(@RequestBody UserDto userDto) {
        UserDto updateUser = userService.updateUser(userDto);
        return Response.success(updateUser);
    }

    @DeleteMapping(value = "/{userId}")
    public Response<String> deleteUser(@PathVariable("userId") Long id) {
        userService.removeUserById(id);
        return Response.success("delete user success");
    }

    //全名查找
    @GetMapping(value = "/username/{userName}")
    public Response<UserDto> findUserByUserName(@PathVariable("userName") String userName) {
        Optional<UserDto> userDto = userService.findUserByUserName(userName);
        return Response.success(userDto.orElse(null));
    }

    //模糊匹配
    @GetMapping(value = "/fuzzy/username")
    public Response<List<UserDto>> fuzzySearchUsersByUserName(@RequestParam("userName") String userName) {
        List<UserDto> userDtos = userService.fuzzyFindUserByUserName(userName);
        return Response.success(userDtos);
    }

}
