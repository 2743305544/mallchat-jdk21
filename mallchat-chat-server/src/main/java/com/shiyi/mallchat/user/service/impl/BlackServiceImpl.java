package com.shiyi.mallchat.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shiyi.mallchat.user.domain.entity.Black;
import com.shiyi.mallchat.user.mapper.BlackMapper;
import com.shiyi.mallchat.user.service.BlackService;
import org.springframework.stereotype.Service;

/**
* @author 34011
* @description 针对表【black(黑名单)】的数据库操作Service实现
* @createDate 2025-04-13 21:06:53
*/
@Service
public class BlackServiceImpl extends ServiceImpl<BlackMapper, Black>
    implements BlackService {

}




