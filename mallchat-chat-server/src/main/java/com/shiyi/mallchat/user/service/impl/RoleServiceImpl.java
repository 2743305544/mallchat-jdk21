package com.shiyi.mallchat.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shiyi.mallchat.user.domain.entity.Role;
import com.shiyi.mallchat.user.domain.enums.RoleEnum;
import com.shiyi.mallchat.user.mapper.RoleMapper;
import com.shiyi.mallchat.user.service.RoleService;
import com.shiyi.mallchat.user.service.cache.UserCache;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
* @author 34011
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2025-04-13 21:06:53
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService {

    @Resource
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        var roles = userCache.getRoleByUid(uid);
        return isAdmin(roles) || roles.contains(roleEnum.getId());
    }

    private boolean isAdmin(Set<Long> roles) {
        return roles.contains(RoleEnum.ADMIN.getId());
    }
}




