package com.shiyi.mallchat.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shiyi.mallchat.user.domain.entity.Role;
import com.shiyi.mallchat.user.domain.enums.RoleEnum;

/**
* @author 34011
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2025-04-13 21:06:53
*/
public interface RoleService extends IService<Role> {

    boolean hasPower(Long uid, RoleEnum roleEnum);
}
