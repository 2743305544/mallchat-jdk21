package com.shiyi.mallchat.user.controller;


import com.shiyi.mallchat.common.domain.dto.RequestInfo;
import com.shiyi.mallchat.common.domain.vo.resp.ApiResult;
import com.shiyi.mallchat.common.interceptor.TokenInterceptor;
import com.shiyi.mallchat.common.utils.AssertUtil;
import com.shiyi.mallchat.common.utils.RequestHolder;
import com.shiyi.mallchat.user.domain.enums.IdempotentEnum;
import com.shiyi.mallchat.user.domain.enums.ItemEnum;
import com.shiyi.mallchat.user.domain.enums.RoleEnum;
import com.shiyi.mallchat.user.domain.vo.req.BlackReq;
import com.shiyi.mallchat.user.domain.vo.req.ModifyNameReq;
import com.shiyi.mallchat.user.domain.vo.req.WearingBadgeReq;
import com.shiyi.mallchat.user.domain.vo.resp.BadgeResp;
import com.shiyi.mallchat.user.domain.vo.resp.UserInfoResp;
import com.shiyi.mallchat.user.service.RoleService;
import com.shiyi.mallchat.user.service.UserBackpackService;
import com.shiyi.mallchat.user.service.UserService;
import com.shiyi.mallchat.user.service.impl.UserBackpackServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capi/user")
@Tag(name = "用户模块")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Resource
    private RoleService roleService;

    @GetMapping("/userInfo")
    @Operation(summary = "获取用户信息")
    public ApiResult<UserInfoResp> getUserInfo(HttpServletRequest request) {
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }


    @PutMapping("/name")
    @Operation(summary = "修改昵称")
    public ApiResult<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
        RequestInfo requestInfo = RequestHolder.get();
        userService.modifyName(requestInfo.getUid(), req.getName());
        return ApiResult.success();
    }

    @GetMapping("/badges")
    @Operation(summary = "获取用户徽章预览")
    public ApiResult<List<BadgeResp>> getBadges() {
        return ApiResult.success(userService.badges(RequestHolder.get().getUid()));
    }

    @PutMapping("/badge")
    @Operation(summary = "佩戴徽章")
    public ApiResult<Void> wearBadge(@Valid @RequestBody WearingBadgeReq req) {
        RequestInfo requestInfo = RequestHolder.get();
        userService.wearBadge(requestInfo.getUid(), req.getItemId());
        return ApiResult.success();
    }

    @Resource
    private UserBackpackService userBackpackService;
    @GetMapping("/public/test")
    public ApiResult<Void> getIdempotent() {
        userBackpackService.acquireItem(11001L, ItemEnum.PLANET.getId(), IdempotentEnum.UID, 11001L+"");
        return ApiResult.success();
    }

    @PutMapping("/black")
    @Operation(summary = "拉黑用户")
    public ApiResult<Void> black(@Valid @RequestBody BlackReq req) {
        RequestInfo requestInfo = RequestHolder.get();
        var isAdmin = roleService.hasPower(requestInfo.getUid(), RoleEnum.ADMIN);
        AssertUtil.isTrue(isAdmin, "权限不足");
        userService.black(req);
        return ApiResult.success();
    }

}
