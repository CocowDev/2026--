package com.hotel.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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

    @TableField("guestName")
    private String guestName;

    @TableField("phone")
    private String guestPhone;

    @TableField("email")
    private String guestEmail;

    @TableField("checkInDate")
    private String checkInDate;

    @TableField("checkOutDate")
    private String checkOutDate;

    @TableField("guests")
    private Integer guestCount;

    @TableField("specialRequests")
    private String specialRequests;

    @TableField("status")
    private String status;

    @TableField("totalPrice")
    private Double totalPrice;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

}
