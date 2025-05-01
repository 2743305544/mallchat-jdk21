package com.shiyi.mallchat.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.user.service.UserService;
import com.shiyi.mallchat.user.service.WXMsgService;
import com.shiyi.mallchat.user.service.adapter.TextBuilder;
import com.shiyi.mallchat.user.service.adapter.UserAdapter;
import com.shiyi.mallchat.websocket.service.WebSocketService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WXMsgServiceImpl implements WXMsgService {

    private static final ConcurrentHashMap<String,Integer> WAIT_AUTHRIZE_MAP = new ConcurrentHashMap<>();

    @Value("${wx.mp.callback}")
    private String callback;

    @Resource
    @Lazy
    private WxMpService wxMpService;

    @Resource
    private WebSocketService webSocketService;

    private static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    @Resource
    private UserService userService;

    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        String openid = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if(code == null) return null;
        // 用户已经注册并授权
        User user = userService.getByOpenId(openid);
        boolean register = Objects.nonNull(user);
        boolean authorized =register && StrUtil.isNotBlank(user.getAvatar());
        if( register && authorized ){
            webSocketService.scanLoginSuccess(code,user.getId());
            return null;
        }
        // 用户未注册
        if(!register){
            User insert = UserAdapter.build(openid);
            userService.register(insert);
        }
        // 推送授权链接
        WAIT_AUTHRIZE_MAP.put(openid,code);
        webSocketService.waitAuthorize(code);
        String authUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        return TextBuilder.build("请点击登录：<a href=\"" + authUrl + "\">登录</a>", wxMpXmlMessage);
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userService.getByOpenId(openid);
        if(StrUtil.isBlank(user.getAvatar())){
            fillUserInfo(user.getId(), userInfo);
        }
        // 找到code
        Integer code = WAIT_AUTHRIZE_MAP.remove(openid);
        webSocketService.scanLoginSuccess(code,user.getId());
    }

    private void fillUserInfo(Long id, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(id, userInfo);
        userService.updateById(user);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            String eventKey = wxMpXmlMessage.getEventKey();
            String code = eventKey.replace("qrscene_", "");
            return Integer.parseInt(code);
        }catch (Exception e){
            log.error("获取code失败:"+ wxMpXmlMessage.getEventKey(),e);
        }
        return null;
    }
}
