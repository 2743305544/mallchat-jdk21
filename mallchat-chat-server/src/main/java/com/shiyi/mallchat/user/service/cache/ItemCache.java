package com.shiyi.mallchat.user.service.cache;

import com.shiyi.mallchat.user.domain.entity.ItemConfig;
import com.shiyi.mallchat.user.service.ItemConfigService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemCache {

    @Resource
    private ItemConfigService itemConfigService;


    @Cacheable(cacheNames = "item" ,key = "'itemByType:'+#itemType")
    public List<ItemConfig> getByType(Integer itemType) {
        return itemConfigService.getByType(itemType);
    }

    @CacheEvict
    public List<ItemConfig> evictByType(Integer type) { return null; }


}
