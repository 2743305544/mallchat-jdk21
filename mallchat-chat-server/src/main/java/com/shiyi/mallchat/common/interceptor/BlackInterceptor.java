package com.shiyi.mallchat.common.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import com.shiyi.mallchat.common.exception.HttpErrorEnum;
import com.shiyi.mallchat.common.utils.RequestHolder;
import com.shiyi.mallchat.user.domain.enums.BlackTypeEnum;
import com.shiyi.mallchat.user.service.cache.UserCache;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Set;

public class BlackInterceptor implements HandlerInterceptor {
    @Resource
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var blackMap = userCache.getBlackMap();
        var requestInfo = RequestHolder.get();
        if(isBlack(requestInfo.getUid(),blackMap.get(BlackTypeEnum.UID.getId()))){
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        };
        if(isBlack(requestInfo.getIp(),blackMap.get(BlackTypeEnum.IP.getId()))){
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        };
        return true;
    }

    private boolean isBlack(Object target, Set<String> strings) {
        if(Objects.isNull(target) || CollectionUtil.isEmpty(strings)){
            return false;
        }
        return strings.contains(target.toString());
    }
}
