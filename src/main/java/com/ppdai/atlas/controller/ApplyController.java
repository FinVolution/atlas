package com.ppdai.atlas.controller;

import com.ppdai.atlas.controller.response.MessageType;
import com.ppdai.atlas.controller.response.Response;
import com.ppdai.atlas.dto.*;
import com.ppdai.atlas.enums.ApplyStatusEnum;
import com.ppdai.atlas.enums.ApplyTypeEnum;
import com.ppdai.atlas.service.*;
import com.ppdai.atlas.vo.PageVO;
import com.ppdai.auth.utils.PauthTokenUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/web/apply")
@Slf4j
public class ApplyController {

    @Autowired
    private PauthTokenUtil pauthTokenUtil;

    @Autowired
    private ApplyService applyService;

    @PostMapping(value = "/createApply")
    public Response<ApplyDto> createApply(@RequestBody ApplyDto applyDto) {
        return Response.success(applyService.create(applyDto));
    }

    @GetMapping(value = "/queryByPage")
    public Response<PageVO<ApplyDto>> queryByPage(@RequestParam(value = "applyUser", required = false) String applyUser,
                                               @RequestParam(value = "status", required = false) String status,
                                               @RequestParam(value = "page") Integer page,
                                               @RequestParam(value = "size") Integer size) {
        PageVO<ApplyDto> sitePageVO = applyService.getByPage(applyUser, status, page, size);
        return Response.mark(MessageType.SUCCESS, sitePageVO);
    }

    @PostMapping(value = "/updateStatus")
    public Response<String> updateStatus(@RequestBody UpdateApplyStatusDto request) {

        ApplyDto applyDto = applyService.getById(request.getApplyId());
        applyDto.setStatus(request.getStatus());
        applyDto.setResult(request.getResult());
        applyDto.setOpUser(pauthTokenUtil.getTokenInfo().getName());

        if (applyDto.getType().equals(ApplyTypeEnum.NEW_APP.name())
                && request.getStatus().equals(ApplyStatusEnum.DONE.name())
                && !applyDto.getStatus().equals(ApplyStatusEnum.DONE.name())) {
            applyService.autoCreateApp(applyDto);
        }

        applyService.update(applyDto);
        return Response.success("OK");
    }
}
