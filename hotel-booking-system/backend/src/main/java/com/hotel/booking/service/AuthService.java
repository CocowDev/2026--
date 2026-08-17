package com.hotel.booking.service;

import com.hotel.booking.dto.LoginDTO;
import com.hotel.booking.dto.LoginResponse;
import com.hotel.booking.dto.RegisterDTO;

/**
 * 认证服务接口
 * 接口与实现分离，便于单元测试 mock 与后续扩展
 */
public interface AuthService {

    /** 用户登录（支持用户名/手机号/邮箱，密码错误时自动降级校验管理员） */
    LoginResponse login(LoginDTO loginDTO);

    /** 管理员登录 */
    LoginResponse adminLogin(LoginDTO loginDTO);

    /** 用户注册（手机号/邮箱唯一性校验） */
    void register(RegisterDTO registerDTO);

}
