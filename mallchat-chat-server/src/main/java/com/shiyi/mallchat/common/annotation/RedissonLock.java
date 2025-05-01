package com.shiyi.mallchat.common.annotation;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface RedissonLock {
    /**
     * key的前缀，默认是取方法全限定名称，可以自己指定
     */
    String prefix() default "";

    /**
     *  key
     */
    String key() default "";

    /**
     * 锁的等待时间，默认不等待
     */
    int waitTime() default -1;

    /**
     * 锁的等待时间单位 默认毫秒
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;


}
