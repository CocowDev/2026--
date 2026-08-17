package com.hotel.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 预订-菜品关联实体
 * 冗余存储 dishName/price，便于预订详情直接展示，避免多表联查
 */
@Data
@TableName("booking_dishes")
public class BookingDish {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("bookingId")
    private Long bookingId;

    @TableField("dishId")
    private Long dishId;

    @TableField("dishName")
    private String dishName;

    @TableField("price")
    private BigDecimal price;

    @TableField("quantity")
    private Integer quantity;

}
