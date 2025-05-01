package com.shiyi.mallchat.user.domain.vo.resp;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BadgeResp {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "图片")
    private String img;

    @Schema(description = "描述")
    private String describe;

    @Schema(description = "是否获得 0未获得 1已获得")
    private Integer obtain;

    @Schema(description = "是否穿戴 0未穿戴 1已穿戴")
    private Integer wearing;
}
