package com.hotel.booking.controller;

import com.hotel.booking.dto.ApiResponse;
import com.hotel.booking.service.BookingService;
import com.hotel.booking.service.UserService;
import com.hotel.booking.vo.BookingVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final BookingService bookingService;
    private final UserService userService;

    public DashboardController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStats() {
        Map<String, Object> stats = bookingService.getStats();
        stats.put("activeUsers", userService.getActiveUsersCount());
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    @GetMapping("/recent-bookings")
    public ResponseEntity<ApiResponse<List<BookingVO>>> getRecentBookings() {
        List<BookingVO> bookings = bookingService.getRecentBookings();
        return ResponseEntity.ok(ApiResponse.success(bookings));
    }

}
