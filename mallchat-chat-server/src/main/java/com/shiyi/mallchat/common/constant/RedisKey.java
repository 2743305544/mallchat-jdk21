package com.shiyi.mallchat.common.constant;

public class RedisKey {
    private static final String BASE_KEY = "mallchat:chat";
    /**
     * 用户token的key
     */
    public static final String USER_TOKEN_STRING = BASE_KEY + "userToken:uid_%d";

    public static String getKey(String key, Object... o) {
        return BASE_KEY + String.format(key, o);
    }
}
