package com.shiyi.mallchat.common.utils;

import com.shiyi.mallchat.common.domain.dto.RequestInfo;

public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }
    public static RequestInfo get() {
        return threadLocal.get();
    }
    public static void remove() {
        threadLocal.remove();
    }
}
