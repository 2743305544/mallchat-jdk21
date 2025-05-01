package com.shiyi.mallchat.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.mallchat.user.domain.entity.UserBackpack;
import com.shiyi.mallchat.user.domain.enums.IdempotentEnum;

import java.util.List;

/**
* @author 34011
* @description 针对表【user_backpack(用户背包表)】的数据库操作Service
* @createDate 2025-04-02 16:54:15
*/
public interface UserBackpackService extends IService<UserBackpack> {

    Integer getCountByVadItemId(Long uid, Long id);

    UserBackpack getFirstVaildItem(Long uid, Long id);

    boolean useItem(UserBackpack firstVaildItem);

    List<UserBackpack> getByItemIds(Long uid, List<Long> list);

    /**
     *  User acquires a certain item
     *  @param uid          User ID  用户id
     *  @param itemId       Item ID  物品id
     *  @param idempotentEnum The uniqueness of the business 幂等类型
     *  @param businessId   Business ID  幂等唯一标识
     */
    void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId);
}
