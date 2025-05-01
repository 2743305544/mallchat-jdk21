package com.shiyi.mallchat.user.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IdempotentEnum {

    UID(1, "uid"),
    MSG_ID(2, "消息Id");
    

    private final Integer type;
    private final String desc;
}
