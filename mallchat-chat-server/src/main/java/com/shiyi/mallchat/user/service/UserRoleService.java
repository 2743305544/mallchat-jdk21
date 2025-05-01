package com.shiyi.mallchat.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.mallchat.user.domain.entity.UserRole;

import java.util.List;

/**
* @author 34011
* @description 针对表【user_role(用户角色关系表)】的数据库操作Service
* @createDate 2025-04-13 21:06:53
*/
public interface UserRoleService extends IService<UserRole> {

    List<UserRole> listByUid(Long uid);
}
