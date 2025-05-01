package com.shiyi.mallchat.user.service.adapter;

import com.shiyi.mallchat.common.domain.enums.YesOrNoEnum;
import com.shiyi.mallchat.user.domain.entity.ItemConfig;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.user.domain.entity.UserBackpack;
import com.shiyi.mallchat.user.domain.vo.resp.BadgeResp;
import com.shiyi.mallchat.user.domain.vo.resp.UserInfoResp;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.BeanUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserAdapter {
    public static User build(String openid) {
        return User.builder().openId(openid).build();
    }

    public static User buildAuthorizeUser(Long id, WxOAuth2UserInfo userInfo) {
        User user=new User();
        user.setId(id);
        user.setAvatar(userInfo.getHeadImgUrl());
        user.setName(userInfo.getNickname());
        return user;
    }

    public static UserInfoResp buildUserInfo(User user, Integer countByVaildItemId) {
        UserInfoResp userInfoResp = new UserInfoResp();
        BeanUtils.copyProperties(user, userInfoResp);
        userInfoResp.setModifyNameChance(countByVaildItemId);
        return userInfoResp;
    }

    public static List<BadgeResp> buildBadgeResp(List<ItemConfig> itemConfigs, List<UserBackpack> backpacks, User user) {
        Set<Long> obtainItemSet = backpacks.stream().map(UserBackpack::getItemId).collect(Collectors.toSet());
        return itemConfigs.stream().map(a->{
            BadgeResp badgeResp = new BadgeResp();
            BeanUtils.copyProperties(a, badgeResp);
            badgeResp.setObtain(obtainItemSet.contains(a.getId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
            badgeResp.setWearing(Objects.equals(a.getId(), user.getItemId()) ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus());
            return badgeResp;
        }).sorted(Comparator.comparing(BadgeResp::getWearing,Comparator.reverseOrder())
                .thenComparing(BadgeResp::getObtain,Comparator.reverseOrder())).toList();
    }
}
