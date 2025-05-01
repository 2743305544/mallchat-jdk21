package com.shiyi.mallchat.common.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RequestInfo {
    private Long uid;
    private String ip;
}
