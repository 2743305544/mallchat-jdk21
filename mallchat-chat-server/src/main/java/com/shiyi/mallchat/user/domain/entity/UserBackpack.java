package com.shiyi.mallchat.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户背包表
 * @TableName user_backpack
 */
@TableName(value ="user_backpack")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBackpack {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * uid
     */
    private Long uid;

    /**
     * 物品id
     */
    private Long itemId;

    /**
     * 使用状态 0.待使用 1已使用
     */
    private Integer status;

    /**
     * 幂等号
     */
    private String idempotent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
