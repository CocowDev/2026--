package com.hotel.booking.service;

import com.hotel.booking.dto.LoginDTO;
import com.hotel.booking.dto.LoginResponse;
import com.hotel.booking.dto.RegisterDTO;
import com.hotel.booking.entity.Admin;
import com.hotel.booking.entity.User;
import com.hotel.booking.mapper.AdminMapper;
import com.hotel.booking.mapper.UserMapper;
import com.hotel.booking.security.JwtUtil;
import com.hotel.booking.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AuthService 单元测试
 * 覆盖：用户登录（成功/密码错误/管理员兜底）、管理员登录、注册（手机号/邮箱唯一性、成功入库）
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private AdminMapper adminMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    /** 构造一个测试用户（password 为已加密串） */
    private User buildUser() {
        User user = new User();
        user.setId(1L);
        user.setName("tom");
        user.setPhone("13500000000");
        user.setEmail("tom@test.com");
        user.setPassword("encoded-password");
        return user;
    }

    private LoginDTO buildLoginDTO(String username, String password) {
        LoginDTO dto = new LoginDTO();
        dto.setUsername(username);
        dto.setPassword(password);
        return dto;
    }

    @Test
    void login_用户账号密码正确_返回登录凭证() {
        // 模拟：用户名命中用户、密码匹配、签发 token
        when(userMapper.findByPhoneOrEmail("tom")).thenReturn(buildUser());
        when(passwordEncoder.matches("abc123", "encoded-password")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "user")).thenReturn("jwt-token");

        LoginResponse response = authService.login(buildLoginDTO("tom", "abc123"));

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertFalse(response.getIsAdmin());
        @SuppressWarnings("unchecked")
        Map<String, Object> userData = (Map<String, Object>) response.getUser();
        assertEquals("tom", userData.get("name"));
        assertEquals("13500000000", userData.get("phone"));
    }

    @Test
    void login_密码错误_抛出异常() {
        when(userMapper.findByPhoneOrEmail("tom")).thenReturn(buildUser());
        when(passwordEncoder.matches("wrong", "encoded-password")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(buildLoginDTO("tom", "wrong")));
        assertEquals("用户名或密码错误", ex.getMessage());
    }

    @Test
    void login_用户不存在_降级校验管理员并成功() {
        when(userMapper.findByPhoneOrEmail("admin")).thenReturn(null);
        Admin admin = new Admin();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setPassword("admin-encoded");
        when(adminMapper.selectOne(any())).thenReturn(admin);
        when(passwordEncoder.matches("admin123", "admin-encoded")).thenReturn(true);
        when(jwtUtil.generateToken(2L, "admin")).thenReturn("admin-token");

        LoginResponse response = authService.login(buildLoginDTO("admin", "admin123"));

        assertTrue(response.getIsAdmin());
        assertEquals("admin-token", response.getToken());
    }

    @Test
    void adminLogin_管理员密码正确_返回管理员凭证() {
        Admin admin = new Admin();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setPassword("admin-encoded");
        when(adminMapper.selectOne(any())).thenReturn(admin);
        when(passwordEncoder.matches("admin123", "admin-encoded")).thenReturn(true);
        when(jwtUtil.generateToken(2L, "admin")).thenReturn("admin-token");

        LoginResponse response = authService.adminLogin(buildLoginDTO("admin", "admin123"));

        assertTrue(response.getIsAdmin());
        assertEquals("admin-token", response.getToken());
        @SuppressWarnings("unchecked")
        Map<String, Object> adminData = (Map<String, Object>) response.getUser();
        assertEquals("admin", adminData.get("username"));
    }

    @Test
    void adminLogin_密码错误_抛出异常() {
        Admin admin = new Admin();
        admin.setId(2L);
        admin.setUsername("admin");
        admin.setPassword("admin-encoded");
        when(adminMapper.selectOne(any())).thenReturn(admin);
        when(passwordEncoder.matches("wrong", "admin-encoded")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.adminLogin(buildLoginDTO("admin", "wrong")));
    }

    @Test
    void register_手机号已存在_抛出异常() {
        when(userMapper.selectOne(any())).thenReturn(buildUser());

        RegisterDTO dto = buildRegisterDTO();
        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(dto));
        assertEquals("该手机号已被注册", ex.getMessage());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void register_邮箱已存在_抛出异常() {
        // 第一次 selectOne 查手机号返回 null，第二次查邮箱命中
        when(userMapper.selectOne(any())).thenReturn(null).thenReturn(buildUser());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.register(buildRegisterDTO()));
        assertEquals("该邮箱已被注册", ex.getMessage());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void register_信息合法_加密密码并入库() {
        when(userMapper.selectOne(any())).thenReturn(null);
        when(passwordEncoder.encode("abc123")).thenReturn("hashed-password");

        authService.register(buildRegisterDTO());

        verify(userMapper).insert(argThat(user ->
                "tom".equals(user.getName()) &&
                "13500000000".equals(user.getPhone()) &&
                "tom@test.com".equals(user.getEmail()) &&
                "hashed-password".equals(user.getPassword())));
    }

    private RegisterDTO buildRegisterDTO() {
        RegisterDTO dto = new RegisterDTO();
        dto.setName("tom");
        dto.setPhone("13500000000");
        dto.setEmail("tom@test.com");
        dto.setPassword("abc123");
        return dto;
    }
}
