package com.shiyi.mallchat.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.mallchat.user.domain.entity.ItemConfig;
import com.shiyi.mallchat.user.mapper.ItemConfigMapper;
import com.shiyi.mallchat.user.service.ItemConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 34011
* @description 针对表【item_config(功能物品配置表)】的数据库操作Service实现
* @createDate 2025-04-02 16:54:15
*/
@Service
public class ItemConfigServiceImpl extends ServiceImpl<ItemConfigMapper, ItemConfig>
    implements ItemConfigService {

    @Override
    public List<ItemConfig> getByType(Integer type) {
        LambdaQueryWrapper<ItemConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ItemConfig::getType, type);
        return list(lambdaQueryWrapper);
    }
}




