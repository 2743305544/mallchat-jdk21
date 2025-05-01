package com.shiyi.mallchat.user.service.cache;

import com.shiyi.mallchat.user.domain.entity.Black;
import com.shiyi.mallchat.user.domain.entity.ItemConfig;
import com.shiyi.mallchat.user.domain.entity.UserRole;
import com.shiyi.mallchat.user.service.BlackService;
import com.shiyi.mallchat.user.service.ItemConfigService;
import com.shiyi.mallchat.user.service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 34011
 */
@Component
public class UserCache {


    @Resource
    private UserRoleService userRoleService;

    @Resource
    private BlackService blackService;


    @Cacheable(cacheNames = "user" ,key = "'roles:'+#uid")
    public Set<Long> getRoleByUid(Long uid) {
        var userRoles= userRoleService.listByUid(uid);
        return userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
    }

    @Cacheable(cacheNames = "user" ,key = "'blackList'")
    public Map<Integer,Set<String>> getBlackMap() {
        var blackMap = blackService.list().stream().collect(Collectors.groupingBy(Black::getType));
        var result = new HashMap<Integer,Set<String>>();
        blackMap.forEach((k,v)->{
            result.put(k,v.stream().map(Black::getTarget).collect(Collectors.toSet()));
        });
        return result;
    }

    @CacheEvict(cacheNames = "user" ,key = "'blackList'")
    public Map<Integer,Set<String>> evitBlackMap() {
        return null;
    }
}
