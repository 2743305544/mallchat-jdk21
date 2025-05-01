package com.shiyi.mallchat.common.exception;

import com.shiyi.mallchat.common.domain.vo.resp.ApiResult;
import com.shiyi.mallchat.common.utils.JsonUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DENIED(401, "登录失效请重新登录");
    private Integer httpCode;
    private String message;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(httpCode, message)));

    }
}
