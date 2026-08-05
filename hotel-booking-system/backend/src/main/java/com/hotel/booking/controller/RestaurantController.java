package com.hotel.booking.controller;

import com.hotel.booking.dto.ApiResponse;
import com.hotel.booking.entity.Restaurant;
import com.hotel.booking.entity.User;
import com.hotel.booking.mapper.RestaurantMapper;
import com.hotel.booking.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantMapper restaurantMapper;
    private final UserMapper userMapper;

    public RestaurantController(RestaurantMapper restaurantMapper, UserMapper userMapper) {
        this.restaurantMapper = restaurantMapper;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Restaurant>>> list() {
        List<Restaurant> restaurants = restaurantMapper.selectList(null);
        return ResponseEntity.ok(ApiResponse.success(restaurants));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Restaurant>> detail(@PathVariable Long id) {
        Restaurant restaurant = restaurantMapper.selectById(id);
        if (restaurant == null) {
            return ResponseEntity.ok(ApiResponse.notFound("餐厅不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(restaurant));
    }

    // 餐饮预订：复用 bookings 表（type='restaurant'），用用户信息补全 phone/email/guestName
    @PostMapping("/book")
    public ResponseEntity<ApiResponse<Void>> book(@RequestBody Map<String, Object> data) {
        Object userIdObj = data.get("userId");
        Long userId = userIdObj == null ? null : ((Number) userIdObj).longValue();
        if (userId == null) {
            return ResponseEntity.ok(ApiResponse.unauthorized("请先登录"));
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            return ResponseEntity.ok(ApiResponse.badRequest("用户不存在"));
        }
        data.put("guestName", user.getName());
        data.put("phone", user.getPhone());
        data.put("email", user.getEmail());
        restaurantMapper.insertRestaurantReservation(data);
        return ResponseEntity.ok(ApiResponse.success("预订成功", null));
    }

}
