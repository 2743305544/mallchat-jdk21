package com.shiyi.mallchat.websocket.service.adapter;

import com.shiyi.mallchat.common.domain.enums.YesOrNoEnum;
import com.shiyi.mallchat.user.domain.entity.User;
import com.shiyi.mallchat.websocket.domain.enums.WSRespTypeEnum;
import com.shiyi.mallchat.websocket.domain.vo.resp.WSBaseResp;
import com.shiyi.mallchat.websocket.domain.vo.resp.WSBlack;
import com.shiyi.mallchat.websocket.domain.vo.resp.WSLoginSuccess;
import com.shiyi.mallchat.websocket.domain.vo.resp.WSLoginUrl;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;

public class WebSocketAdapter {
    public static WSBaseResp<?> buildResp(WxMpQrCodeTicket wxMpQrCodeTicket) {
        WSBaseResp<WSLoginUrl> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_URL.getType());
        resp.setData(new WSLoginUrl(wxMpQrCodeTicket.getUrl()));
        return resp;
    }

    public static WSBaseResp<?> buildResp(User user, String token, boolean b) {
        WSBaseResp<WSLoginSuccess> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SUCCESS.getType());
        resp.setData(new WSLoginSuccess(user.getId(), user.getAvatar(), token, user.getName(), b ? YesOrNoEnum.YES.getStatus() : YesOrNoEnum.NO.getStatus()));
        return resp;
    }

    public static WSBaseResp<?> buildWaitAuthorizeResp() {
        WSBaseResp<?> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.LOGIN_SCAN_SUCCESS.getType());
        return resp;
    }

    public static WSBaseResp<?> buildInvalidResp() {
        WSBaseResp<?> resp = new WSBaseResp<>();
        resp.setType(WSRespTypeEnum.INVALIDATE_TOKEN.getType());
        return resp;
    }

    public static WSBaseResp<?> buildBlack(User user) {
        var resp = new WSBaseResp<WSBlack>();
        resp.setType(WSRespTypeEnum.BLACK.getType());
        resp.setData(new WSBlack(user.getId()));
        return resp;
    }
}
