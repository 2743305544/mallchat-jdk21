package com.shiyi.mallchat.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.mallchat.user.domain.entity.ItemConfig;

import java.util.List;

/**
* @author 34011
* @description 针对表【item_config(功能物品配置表)】的数据库操作Service
* @createDate 2025-04-02 16:54:15
*/
public interface ItemConfigService extends IService<ItemConfig> {

    List<ItemConfig> getByType(Integer type);
}
