package com.shiyi.mallchat.websocket.service;

import com.shiyi.mallchat.websocket.domain.vo.resp.WSBaseResp;
import io.netty.channel.Channel;

public interface WebSocketService {
    void connect(Channel channel);

    void handleLoginReq(Channel channel);

    void remove(Channel channel);

    void scanLoginSuccess(Integer code, Long id);

    void waitAuthorize(Integer code);

    void authorize(Channel channel, String data);

    void sendMsgToAll(WSBaseResp<?> resp);
}
