package com.shiyi.mallchat.websocket.utlis;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class NettyUtil {

    public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");

    public static AttributeKey<String> IP = AttributeKey.valueOf("ip");
    public static <T> void setAttr(Channel channel, AttributeKey<T> key, T value) {
        channel.attr(key).set(value);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> key) {
        return channel.attr(key).get();
    }
}
