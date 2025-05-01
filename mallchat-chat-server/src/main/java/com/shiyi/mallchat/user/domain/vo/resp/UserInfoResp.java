package com.shiyi.mallchat.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
public class UserInfoResp {
    @Schema(description = "id")
    private Long id;
    @Schema(description = "昵称")
    private String name;
    @Schema(description = "头像")
    private String avatar;
    @Schema(description = "性别 1男 2女")
    private Integer sex;
    @Schema(description = "头像修改机会")
    private Integer modifyNameChance;
}
