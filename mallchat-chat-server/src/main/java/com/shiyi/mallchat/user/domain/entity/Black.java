package com.shiyi.mallchat.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 黑名单
 * @TableName black
 */
@TableName(value ="black")
@Data
public class Black {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 拉黑目标类型 1.ip 2uid
     */
    private Integer type;

    /**
     * 拉黑目标
     */
    private String target;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
