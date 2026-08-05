package com.hotel.booking.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotel.booking.dto.ApiResponse;
import com.hotel.booking.entity.User;
import com.hotel.booking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        IPage<User> userPage = userService.getUsers(page, pageSize);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", userPage.getRecords());
        result.put("total", userPage.getTotal());
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> detail(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.notFound("用户不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user);
        return ResponseEntity.ok(ApiResponse.success("更新成功", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

}
