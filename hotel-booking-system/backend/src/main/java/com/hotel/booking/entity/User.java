package com.hotel.booking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /**
     * 密码敏感字段脱敏：@JsonIgnore 使该字段在 JSON 序列化（接口返回体）
     * 与反序列化（请求体）时均被忽略，防止密码泄漏。
     * 登录/注册的密码校验在 Service 内部通过 getPassword() 读取，不受影响；
     * 更新用户时 UserService 会保留原密码，请求体中的 password 一律忽略。
     */
    @JsonIgnore
    @TableField("password")
    private String password;

    @TableField("createdAt")
    private LocalDateTime createdAt;

    @TableField("updatedAt")
    private LocalDateTime updatedAt;

}
