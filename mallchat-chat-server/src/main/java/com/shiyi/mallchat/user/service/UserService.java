package com.shiyi.mallchat.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.user.domain.enums.IdempotentEnum;
import com.shiyi.mallchat.user.domain.vo.req.BlackReq;
import com.shiyi.mallchat.user.domain.vo.req.ModifyNameReq;
import com.shiyi.mallchat.user.domain.vo.resp.BadgeResp;
import com.shiyi.mallchat.user.domain.vo.resp.UserInfoResp;
import jakarta.validation.Valid;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author 34011
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-03-29 12:11:35
*/
public interface UserService extends IService<User> {

    User getByOpenId(String openid);

    Long register(User insert);

    UserInfoResp getUserInfo(Long uid);

    void modifyName(Long uid, String name);

    List<BadgeResp> badges(Long uid);

    void wearBadge(Long uid, @NotNull Long itemId);

    void wearingBadge(Long uid, Long itemId);


    void black(@Valid BlackReq req);

    void invaildUid(Long id);
}
