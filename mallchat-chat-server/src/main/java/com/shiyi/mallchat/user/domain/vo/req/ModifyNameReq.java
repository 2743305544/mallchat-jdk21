package com.shiyi.mallchat.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ModifyNameReq {
    @Schema(description = "昵称")
    @NotBlank
    @Length(min = 1, max = 6,message = "昵称长度为1-6")
    private String name;
}
