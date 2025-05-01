package com.shiyi.mallchat.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BlackReq {
    @Schema(description = "拉黑用户的id")
    @NotBlank(message = "用户id不能为空")
    private String uid;
}
