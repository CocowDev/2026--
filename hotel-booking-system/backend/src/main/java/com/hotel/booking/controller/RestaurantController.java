package com.hotel.booking.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hotel.booking.dto.ApiResponse;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.entity.BookingDish;
import com.hotel.booking.entity.Dish;
import com.hotel.booking.entity.Restaurant;
import com.hotel.booking.entity.User;
import com.hotel.booking.mapper.BookingDishMapper;
import com.hotel.booking.mapper.BookingMapper;
import com.hotel.booking.mapper.DishMapper;
import com.hotel.booking.mapper.RestaurantMapper;
import com.hotel.booking.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantMapper restaurantMapper;
    private final UserMapper userMapper;
    private final DishMapper dishMapper;
    private final BookingDishMapper bookingDishMapper;
    private final BookingMapper bookingMapper;

    public RestaurantController(RestaurantMapper restaurantMapper, UserMapper userMapper,
                                DishMapper dishMapper, BookingDishMapper bookingDishMapper,
                                BookingMapper bookingMapper) {
        this.restaurantMapper = restaurantMapper;
        this.userMapper = userMapper;
        this.dishMapper = dishMapper;
        this.bookingDishMapper = bookingDishMapper;
        this.bookingMapper = bookingMapper;
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

    /**
     * 餐厅菜品列表（餐饮预订第二步：选择餐厅后点选菜品）
     */
    @GetMapping("/{id}/dishes")
    public ResponseEntity<ApiResponse<List<Dish>>> dishes(@PathVariable Long id) {
        List<Dish> dishes = dishMapper.selectList(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getRestaurantId, id)
                .orderByAsc(Dish::getPrice));
        return ResponseEntity.ok(ApiResponse.success(dishes));
    }

    /**
     * 餐饮预订：复用 bookings 表（type='restaurant'），用用户信息补全 phone/email/guestName；
     * 携带 dishes 时写入 booking_dishes 关联表并计算菜品总价。
     * @Transactional：预订主记录 + 菜品关联 + 总价回填三步在同一事务内，任一步失败整体回滚
     */
    @PostMapping("/book")
    @Transactional(rollbackFor = Exception.class)
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

        // —— 写入所选菜品并计算总价 ——
        Long bookingId = data.get("id") == null ? null : ((Number) data.get("id")).longValue();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> dishes = (List<Map<String, Object>>) data.get("dishes");
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (bookingId != null && dishes != null && !dishes.isEmpty()) {
            for (Map<String, Object> dishReq : dishes) {
                Long dishId = ((Number) dishReq.get("dishId")).longValue();
                int quantity = dishReq.get("quantity") == null ? 1 : ((Number) dishReq.get("quantity")).intValue();
                Dish dish = dishMapper.selectById(dishId);
                if (dish == null) {
                    continue;
                }
                BookingDish bookingDish = new BookingDish();
                bookingDish.setBookingId(bookingId);
                bookingDish.setDishId(dishId);
                bookingDish.setDishName(dish.getName());
                bookingDish.setPrice(dish.getPrice());
                bookingDish.setQuantity(quantity);
                bookingDishMapper.insert(bookingDish);
                totalPrice = totalPrice.add(dish.getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
            // 回填菜品总价
            Booking booking = bookingMapper.selectById(bookingId);
            if (booking != null && totalPrice.compareTo(BigDecimal.ZERO) > 0) {
                booking.setTotalPrice(totalPrice);
                booking.setUpdatedAt(LocalDateTime.now());
                bookingMapper.updateById(booking);
            }
        }
        return ResponseEntity.ok(ApiResponse.success("预订成功", null));
    }

}
