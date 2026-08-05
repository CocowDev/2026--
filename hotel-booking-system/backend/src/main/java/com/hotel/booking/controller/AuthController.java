package com.hotel.booking.controller;

import com.hotel.booking.dto.ApiResponse;
import com.hotel.booking.dto.LoginDTO;
import com.hotel.booking.dto.LoginResponse;
import com.hotel.booking.dto.RegisterDTO;
import com.hotel.booking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResponse response = authService.login(loginDTO);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<LoginResponse>> adminLogin(@Valid @RequestBody LoginDTO loginDTO) {
        LoginResponse response = authService.adminLogin(loginDTO);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return ResponseEntity.ok(ApiResponse.success("注册成功", null));
    }

}
