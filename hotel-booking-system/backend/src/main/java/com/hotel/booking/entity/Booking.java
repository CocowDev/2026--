package com.hotel.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("bookings")
public class Booking {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("userId")
    private Long userId;

    @TableField("roomTypeId")
    private Long roomTypeId;

    // 预订类型：room-客房 / restaurant-餐饮（餐饮预订由 RestaurantMapper 写入）
    @TableField("type")
    private String type;

    @TableField("guestName")
    private String guestName;

    @TableField("phone")
    private String guestPhone;

    @TableField("email")
    private String guestEmail;

    // 入住日期：对应 DB datetime 列，使用 LocalDate（仅日期，JSON 序列化为 yyyy-MM-dd）
    @TableField("checkInDate")
    private LocalDate checkInDate;

    // 离店日期：同上，LocalDate
    @TableField("checkOutDate")
    private LocalDate checkOutDate;

    @TableField("guests")
    private Integer guestCount;

    @TableField("specialRequests")
    private String specialRequests;

    @TableField("status")
    private String status;

    // 订单总价：对应 DB decimal(10,2)，使用 BigDecimal 避免浮点精度误差
    @TableField("totalPrice")
    private BigDecimal totalPrice;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

}
