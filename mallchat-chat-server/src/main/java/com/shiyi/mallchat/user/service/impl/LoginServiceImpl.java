package com.shiyi.mallchat.user.service.impl;

import com.shiyi.mallchat.common.constant.RedisKey;
import com.shiyi.mallchat.common.utils.JwtUtils;
import com.shiyi.mallchat.common.utils.RedisUtils;
import com.shiyi.mallchat.user.service.LoginService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private static final long TOKEN_EXPIRE_DAYS = 3;
    @Resource
    private JwtUtils jwtUtils;


    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        Long validUid = getValidUid(token);
        String userTokenKey = getUserTokenKey(validUid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        if(expireDays  == -2) return;
        if(expireDays< 1) RedisUtils.expire(getUserTokenKey(validUid), TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(uid), token,TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (uid == null) return null;
        String oldToken = RedisUtils.getStr(getUserTokenKey(uid));
        if(StringUtils.isBlank(oldToken)) return null;
        return Objects.equals(token, oldToken) ? uid : null;
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }
}
