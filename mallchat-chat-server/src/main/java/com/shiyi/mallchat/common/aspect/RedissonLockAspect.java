package com.shiyi.mallchat.common.aspect;

import com.shiyi.mallchat.common.annotation.RedissonLock;
import com.shiyi.mallchat.common.service.LockService;
import com.shiyi.mallchat.common.utils.SpElUtils;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(0)
public class RedissonLockAspect {

    @Resource
    private LockService lockService;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint point, RedissonLock redissonLock) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String prefix = StringUtils.isBlank(redissonLock.prefix()) ? SpElUtils.getMethodKey(signature.getMethod()) : redissonLock.prefix();
        String key = SpElUtils.parseSpEL(signature,point.getArgs(),redissonLock.key());
        return lockService.executeWithLock(prefix + ":" + key, redissonLock.waitTime(), redissonLock.timeUnit(), point::proceed);
    }
}
