package com.shiyi.mallchat.common.interceptor;

import com.shiyi.mallchat.common.exception.HttpErrorEnum;
import com.shiyi.mallchat.user.service.LoginService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;


@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_SCHEMA = "Bearer";
    public static final String UID = "uid";

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        if (validUid != null) {
            request.setAttribute(UID, validUid);
        }else {
            boolean isPublic = isPublicUrl(request);
            if(!isPublic) {
                // 401 未授权
                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }
        }
        return true;
    }

    private static boolean isPublicUrl(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        boolean isPublic = split.length >2 && "public".equals(split[3]);
        return isPublic;
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(s -> s.startsWith(AUTHORIZATION_SCHEMA + " "))
                .map(s -> s.substring(AUTHORIZATION_SCHEMA.length() + 1))
                .orElse(null);
    }
}
