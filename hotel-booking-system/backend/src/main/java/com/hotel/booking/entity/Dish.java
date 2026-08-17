package com.hotel.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 餐厅菜品实体（餐饮预订第二步：选择餐厅后点选菜品）
 */
@Data
@TableName("dishes")
public class Dish {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("restaurantId")
    private Long restaurantId;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("price")
    private BigDecimal price;

    @TableField("imageUrl")
    private String imageUrl;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

}
