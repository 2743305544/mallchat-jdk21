package com.shiyi.mallchat.common.service;

import com.shiyi.mallchat.common.domain.enums.CommonErrorEnum;
import com.shiyi.mallchat.common.exception.BusinessException;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class LockService {

    @Resource
    private RedissonClient redissonClient;

    @SneakyThrows
    public <T> T executeWithLock(String lockKey, int waitTime , TimeUnit timeUnit, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean success = lock.tryLock(waitTime, timeUnit);
        if (!success) {
            throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try {
            return supplier.get();
        }finally {
            lock.unlock();
        }
    }


    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        return executeWithLock(lockKey,-1,TimeUnit.MILLISECONDS,supplier);
    }

    public <T> T executeWithLock(String lockKey, Runnable runnable) {
        return executeWithLock(lockKey,-1,TimeUnit.MILLISECONDS,()->{
            runnable.run();
            return null;
        });
    }

    @FunctionalInterface
    public interface Supplier<T> {
        T get() throws Throwable;
    }
}
