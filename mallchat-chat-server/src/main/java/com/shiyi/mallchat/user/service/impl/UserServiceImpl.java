package com.shiyi.mallchat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.mallchat.common.domain.enums.YesOrNoEnum;
import com.shiyi.mallchat.common.event.UserBlackEvent;
import com.shiyi.mallchat.common.event.UserRegisterEvent;
import com.shiyi.mallchat.common.utils.AssertUtil;
import com.shiyi.mallchat.user.domain.entity.*;
import com.shiyi.mallchat.user.domain.enums.BlackTypeEnum;
import com.shiyi.mallchat.user.domain.enums.ItemEnum;
import com.shiyi.mallchat.user.domain.enums.ItemTypeEnum;
import com.shiyi.mallchat.user.domain.vo.req.BlackReq;
import com.shiyi.mallchat.user.domain.vo.resp.BadgeResp;
import com.shiyi.mallchat.user.domain.vo.resp.UserInfoResp;
import com.shiyi.mallchat.user.mapper.UserMapper;
import com.shiyi.mallchat.user.service.BlackService;
import com.shiyi.mallchat.user.service.ItemConfigService;
import com.shiyi.mallchat.user.service.UserBackpackService;
import com.shiyi.mallchat.user.service.UserService;
import com.shiyi.mallchat.user.service.adapter.UserAdapter;
import com.shiyi.mallchat.user.service.cache.ItemCache;
import jakarta.annotation.Resource;
import jodd.util.StringUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
* @author 34011
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-03-29 12:11:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserBackpackService userBackpackService;

    @Resource
    private ItemCache itemCache;

    @Resource
    private ItemConfigService itemConfigService;


    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private BlackService blackService;

    @Override
    public User getByOpenId(String openid) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getOpenId, openid));
    }

    @Override
    @Transactional
    public Long register(User insert) {
        boolean save = save(insert);
        // todo 用户注册事件
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this,insert));
        return save ? insert.getId() : null;
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = getById(uid);
        Integer countByValidItemId = userBackpackService.getCountByVadItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user,countByValidItemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyName(Long uid, String name) {
        User oldUser = getByName(name);
        AssertUtil.isEmpty(oldUser, "昵称已存在,请换一个试试");
        UserBackpack firstVaildItem = userBackpackService.getFirstVaildItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(firstVaildItem, "没有名字修改卡");
        boolean success = userBackpackService.useItem(firstVaildItem);
        if(success) {
            tomodifyName(uid, name);
        }
    }

    @Override
    public List<BadgeResp> badges(Long uid) {
        List<ItemConfig> itemConfigs = itemCache.getByType(ItemTypeEnum.BADGE.getType());
        List<UserBackpack> backpacks = userBackpackService.getByItemIds(uid, itemConfigs.stream().map(ItemConfig::getId).toList());
        User user = getById(uid);
        return UserAdapter.buildBadgeResp(itemConfigs, backpacks, user);
    }

    @Override
    public void wearBadge(Long uid, Long itemId) {
        UserBackpack firstVaildItem = userBackpackService.getFirstVaildItem(uid, itemId);
        AssertUtil.isNotEmpty(firstVaildItem, "没有该徽章");
        ItemConfig itemConfig = itemConfigService.getById(firstVaildItem.getItemId());
        AssertUtil.equal(itemConfig.getType(), ItemTypeEnum.BADGE.getType(), "不是徽章");
        wearingBadge(uid, firstVaildItem.getItemId());
    }

    @Override
    public void wearingBadge(Long uid, Long itemId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, uid);
        User user = new User();
        user.setItemId(itemId);
        update(user, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void black(BlackReq req) {
        var uid = req.getUid();
        var user = new Black();
        user.setType(BlackTypeEnum.UID.getId());
        user.setTarget(uid);
        blackService.save(user);
        var byId = getById(uid);
        blackIp(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getCreateIp).orElse(null));
        blackIp(Optional.ofNullable(byId.getIpInfo()).map(IpInfo::getUpdateIp).orElse(null));
        applicationEventPublisher.publishEvent(new UserBlackEvent(this,byId));
    }

    @Override
    public void invaildUid(Long id) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, id);
        User user = new User();
        user.setStatus(YesOrNoEnum.NO.getStatus());
        update(user, lambdaQueryWrapper);
    }

    private void blackIp(String Ip) {
        if(StringUtil.isBlank(Ip)) return;
        try{
            var user = new Black();
            user.setType(BlackTypeEnum.IP.getId());
            user.setTarget(Ip);
            blackService.save(user);
        }catch (Exception ignored){

        }
    }


    private void tomodifyName(Long uid, String name) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, uid);
        User user = new User();
        user.setName(name);
        update(user, lambdaQueryWrapper);
    }

    private User getByName(String name) {
        return getOne(new LambdaQueryWrapper<User>().eq(User::getName, name));
    }
}




