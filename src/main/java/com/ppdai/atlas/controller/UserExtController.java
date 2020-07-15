package com.ppdai.atlas.controller;

import com.ppdai.atlas.dto.Response;
import com.ppdai.atlas.dto.UserExtDto;
import com.ppdai.atlas.service.UserExtService;
//import com.ppdai.authority.annotation.AuthorityAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/web/userexts")
public class UserExtController {

    @Autowired
    private UserExtService userExtService;

    @PostMapping(name = "/")
    public Response<String> addUserExt(@RequestBody UserExtDto userExtDto) {
        userExtService.removeUserExtByWorkNumber(userExtDto.getUserWorkNumber());
        userExtService.addUserExt(userExtDto);
        return Response.success("success");
    }

    @GetMapping(name = "/")
    public Response<UserExtDto> getUserExtDtoByWorkNumber(@RequestParam String workNumber) {
        UserExtDto userExtDto = userExtService.findUserExtByWorkNumber(workNumber);
        return Response.success(userExtDto);
    }

}
