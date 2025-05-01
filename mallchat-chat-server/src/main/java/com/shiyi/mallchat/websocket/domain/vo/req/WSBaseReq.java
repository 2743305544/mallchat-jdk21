package com.shiyi.mallchat.websocket.domain.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSBaseReq {
    private Integer type;
    private String data;

}
