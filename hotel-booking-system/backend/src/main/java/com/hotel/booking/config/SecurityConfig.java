package com.hotel.booking.config;

import com.hotel.booking.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 公开接口：认证、房型、餐厅列表
                        .requestMatchers("/api/auth/**", "/api/room-types/**", "/api/restaurants/**").permitAll()
                        // 预订导出（占位接口）：仅管理员
                        .requestMatchers(HttpMethod.GET, "/api/bookings/export").hasRole("ADMIN")
                        // 预订查询/创建：登录用户即可
                        .requestMatchers(HttpMethod.GET, "/api/bookings/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/bookings").authenticated()
                        // 预订管理操作：仅管理员（PUT/PATCH/DELETE）
                        .requestMatchers(HttpMethod.PUT, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/bookings/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasRole("ADMIN")
                        // 用户管理、仪表盘：仅管理员（对应 UserController/DashboardController 真实路径）
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
