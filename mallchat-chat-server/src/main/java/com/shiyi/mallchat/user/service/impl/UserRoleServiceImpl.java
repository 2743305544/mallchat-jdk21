package com.shiyi.mallchat.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shiyi.mallchat.user.domain.entity.UserRole;
import com.shiyi.mallchat.user.mapper.UserRoleMapper;
import com.shiyi.mallchat.user.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 34011
* @description 针对表【user_role(用户角色关系表)】的数据库操作Service实现
* @createDate 2025-04-13 21:06:53
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService {

    @Override
    public List<UserRole> listByUid(Long uid) {
        return lambdaQuery()
                .eq(UserRole::getUid, uid)
                .list();
    }
}




