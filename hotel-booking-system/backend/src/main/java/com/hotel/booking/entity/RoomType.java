package com.hotel.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("room_types")
public class RoomType {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("title")
    private String title;

    @TableField("description")
    private String description;

    // 每晚单价：对应 DB decimal(10,2)，使用 BigDecimal 保证金额计算精确
    @TableField("price")
    private BigDecimal price;

    @TableField("imageUrl")
    private String imageUrl;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

}
