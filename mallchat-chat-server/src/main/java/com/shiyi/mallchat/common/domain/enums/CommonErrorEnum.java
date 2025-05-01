package com.shiyi.mallchat.common.domain.enums;


import com.shiyi.mallchat.common.exception.ErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonErrorEnum implements ErrorEnum {
    BUSINESS_ERROR(0, "{0}"),
    SYSTEM_ERROR(-1, "系统错误"),
    PARAM_INVALID(-2, "参数校验失败"),

    LOCK_LIMIT(-3, "操作过于频繁,请稍后再试");
    private final Integer code;
    private final String msg;
}
