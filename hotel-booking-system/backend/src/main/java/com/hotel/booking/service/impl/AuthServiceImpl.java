package com.hotel.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotel.booking.dto.LoginDTO;
import com.hotel.booking.dto.LoginResponse;
import com.hotel.booking.dto.RegisterDTO;
import com.hotel.booking.entity.Admin;
import com.hotel.booking.entity.User;
import com.hotel.booking.mapper.AdminMapper;
import com.hotel.booking.mapper.UserMapper;
import com.hotel.booking.security.JwtUtil;
import com.hotel.booking.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务实现：用户/管理员登录、用户注册
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserMapper userMapper, AdminMapper adminMapper,
                           PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
        User user = userMapper.findByPhoneOrEmail(loginDTO.getUsername());
        
        if (user != null && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getId(), "user");
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("name", user.getName());
            userData.put("phone", user.getPhone());
            userData.put("email", user.getEmail());
            return new LoginResponse(token, userData, false);
        }
        
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, loginDTO.getUsername()));
        
        if (admin != null && passwordEncoder.matches(loginDTO.getPassword(), admin.getPassword())) {
            String token = jwtUtil.generateToken(admin.getId(), "admin");
            Map<String, Object> adminData = new HashMap<>();
            adminData.put("id", admin.getId());
            adminData.put("username", admin.getUsername());
            return new LoginResponse(token, adminData, true);
        }
        
        throw new RuntimeException("用户名或密码错误");
    }

    @Override
    public LoginResponse adminLogin(LoginDTO loginDTO) {
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, loginDTO.getUsername()));
        
        if (admin != null && passwordEncoder.matches(loginDTO.getPassword(), admin.getPassword())) {
            String token = jwtUtil.generateToken(admin.getId(), "admin");
            Map<String, Object> adminData = new HashMap<>();
            adminData.put("id", admin.getId());
            adminData.put("username", admin.getUsername());
            return new LoginResponse(token, adminData, true);
        }
        
        throw new RuntimeException("用户名或密码错误");
    }

    /**
     * 用户注册：事务保证用户插入原子性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) {
        User existingUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhone, registerDTO.getPhone()));
        if (existingUser != null) {
            throw new RuntimeException("该手机号已被注册");
        }
        
        existingUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, registerDTO.getEmail()));
        if (existingUser != null) {
            throw new RuntimeException("该邮箱已被注册");
        }
        
        User user = new User();
        user.setName(registerDTO.getName());
        user.setPhone(registerDTO.getPhone());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        userMapper.insert(user);
    }

}
