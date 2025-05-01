package com.shiyi.mallchat.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiyi.mallchat.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 34011
* {@code @description} 针对表【user(用户表)】的数据库操作Mapper
* {@code @createDate} 2025-03-29 12:11:35
* {@code @Entity} generator.domain.User
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
