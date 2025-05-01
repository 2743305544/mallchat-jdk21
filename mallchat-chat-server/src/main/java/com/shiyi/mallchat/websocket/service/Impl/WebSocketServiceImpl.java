package com.shiyi.mallchat.websocket.service.Impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.shiyi.mallchat.common.event.UserOnlineEvent;
import com.shiyi.mallchat.user.domain.entity.IpInfo;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.user.domain.enums.RoleEnum;
import com.shiyi.mallchat.user.service.LoginService;
import com.shiyi.mallchat.user.service.RoleService;
import com.shiyi.mallchat.user.service.UserRoleService;
import com.shiyi.mallchat.user.service.UserService;
import com.shiyi.mallchat.websocket.domain.dto.WSChannelExtraDTO;
import com.shiyi.mallchat.websocket.domain.vo.resp.WSBaseResp;
import com.shiyi.mallchat.websocket.service.WebSocketService;
import com.shiyi.mallchat.websocket.service.adapter.WebSocketAdapter;
import com.shiyi.mallchat.websocket.utlis.NettyUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Resource
    @Lazy
    private WxMpService wxMpService;

    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    public static final int MAXIMUM_SIZE = 1000;
    public static final Duration DURATION = Duration.ofHours(1);
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();

    @Resource
    private UserService userService;

    @Resource
    private LoginService loginService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private RoleService RoleService;
    @Autowired
    private RoleService roleService;

    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }

    @SneakyThrows
    @Override
    public void handleLoginReq(Channel channel) {
        //随机码
        Integer code = generateLoginCode(channel);
        //生成带参二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        //发送
        sendMsg(channel, WebSocketAdapter.buildResp(wxMpQrCodeTicket));
    }

    @Override
    public void remove(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if(Objects.isNull(channel)){
            return;
        }
        User user = userService.getById(uid);
        WAIT_LOGIN_MAP.invalidate(code);
        String token = loginService.login(user.getId());
        LoginSuccess(channel, user, token);
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorizeResp());
    }

    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if(Objects.nonNull(validUid)){
            User user = userService.getById(validUid);
            LoginSuccess(channel, user, token);
        }else {
            sendMsg(channel, WebSocketAdapter.buildInvalidResp());
        }
    }

    @Override
    public void sendMsgToAll(WSBaseResp<?> resp) {
        ONLINE_WS_MAP.forEach((channel, wsChannelExtraDTO) -> Thread.startVirtualThread(() -> sendMsg(channel, resp)));
    }

    private void LoginSuccess(Channel channel, User user, String token) {
        var wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        sendMsg(channel, WebSocketAdapter.buildResp(user, token, roleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER) ));
        //通知用户上线
        user.setLastOptTime(new Date());
        user.refreshIp(NettyUtil.getAttr(channel, NettyUtil.IP));
        applicationEventPublisher.publishEvent(new UserOnlineEvent(this, user));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        }while (WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel) != null);
        return code;
    }
}
