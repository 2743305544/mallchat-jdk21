package com.shiyi.mallchat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.repository.AbstractRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.mallchat.common.annotation.RedissonLock;
import com.shiyi.mallchat.common.domain.enums.YesOrNoEnum;
import com.shiyi.mallchat.common.service.LockService;
import com.shiyi.mallchat.common.utils.AssertUtil;
import com.shiyi.mallchat.user.domain.entity.UserBackpack;
import com.shiyi.mallchat.user.domain.enums.IdempotentEnum;
import com.shiyi.mallchat.user.mapper.UserBackpackMapper;
import com.shiyi.mallchat.user.service.UserBackpackService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 34011
 * @description 针对表【user_backpack(用户背包表)】的数据库操作Service实现
 * @createDate 2025-04-02 16:54:15
 */
@Service
@Slf4j
public class UserBackpackServiceImpl extends ServiceImpl<UserBackpackMapper, UserBackpack>
        implements UserBackpackService {

    @Resource
    private LockService lockService;


    @Override
    public Integer getCountByVadItemId(Long uid, Long itemId) {
        LambdaQueryWrapper<UserBackpack> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus());
        return Math.toIntExact(count(wrapper));
    }

    @Override
    public UserBackpack getFirstVaildItem(Long uid, Long id) {
        LambdaQueryWrapper<UserBackpack> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, id)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .orderByAsc(UserBackpack::getId)
                .last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        ((UserBackpackServiceImpl) AopContext.currentProxy()).doAcquireItem(uid, itemId, idempotent);
    }

    @RedissonLock(key = "#idempotent",waitTime = 5000)
    public void doAcquireItem(Long uid, Long itemId, String idempotent) {
        UserBackpack userBackpack = getByIdempotent(idempotent);
        if (Objects.nonNull((userBackpack))) {
            return;
        }
        UserBackpack insert = UserBackpack.builder()
                .uid(uid)
                .itemId(itemId)
                .status(YesOrNoEnum.NO.getStatus())
                .idempotent(idempotent)
                .build();
        save(insert);
    }

    private UserBackpack getByIdempotent(String idempotent) {
        LambdaQueryWrapper<UserBackpack> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserBackpack::getIdempotent, idempotent);
        return getOne(lambdaQueryWrapper);
    }

    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }

    @Override
    public boolean useItem(UserBackpack firstVaildItem) {
        firstVaildItem.setStatus(YesOrNoEnum.YES.getStatus());
        return updateById(firstVaildItem);
    }

    @Override
    public List<UserBackpack> getByItemIds(Long uid, List<Long> list) {
        LambdaQueryWrapper<UserBackpack> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .in(UserBackpack::getItemId, list);
        return list(wrapper);
    }
}




