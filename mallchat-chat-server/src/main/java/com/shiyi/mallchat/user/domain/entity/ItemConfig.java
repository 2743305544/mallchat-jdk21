package com.shiyi.mallchat.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 功能物品配置表
 * @TableName item_config
 */
@TableName(value ="item_config")
@Data
public class ItemConfig {
    /**
     * id
     */
    @TableId
    private Long id;

    /**
     * 物品类型 1改名卡 2徽章
     */
    private Integer type;

    /**
     * 物品图片
     */
    private String img;

    /**
     * 物品功能描述
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
