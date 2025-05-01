package com.shiyi.mallchat.user.domain.vo.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class WearingBadgeReq {

    @Schema(description = "徽章id")
    @NotNull
    private Long itemId;
}
