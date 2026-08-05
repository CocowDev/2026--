package com.hotel.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("username")
    private String name;

    @TableField("phone")
    private String phone;

    @TableField("email")
    private String email;

    @TableField("password")
    private String password;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

}
