package com.hotel.booking.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotel.booking.dto.ApiResponse;
import com.hotel.booking.dto.BookingDTO;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.entity.RoomType;
import com.hotel.booking.mapper.RoomTypeMapper;
import com.hotel.booking.security.JwtUtil;
import com.hotel.booking.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final RoomTypeMapper roomTypeMapper;
    private final JwtUtil jwtUtil;

    public BookingController(BookingService bookingService, RoomTypeMapper roomTypeMapper, JwtUtil jwtUtil) {
        this.bookingService = bookingService;
        this.roomTypeMapper = roomTypeMapper;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        IPage<Booking> bookingPage = bookingService.getBookings(page, pageSize, status, keyword);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", bookingPage.getRecords());
        result.put("total", bookingPage.getTotal());
        
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Booking>> detail(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.ok(ApiResponse.notFound("预订不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(booking));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable Long id, @Valid @RequestBody BookingDTO bookingDTO) {
        bookingService.updateBooking(id, bookingDTO);
        return ResponseEntity.ok(ApiResponse.success("更新成功", null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(@Valid @RequestBody BookingDTO bookingDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            bookingService.createBooking(bookingDTO, userId);
            return ResponseEntity.ok(ApiResponse.success("预订成功", null));
        }
        return ResponseEntity.ok(ApiResponse.unauthorized("请先登录"));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        bookingService.updateBookingStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("状态更新成功", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }

    @GetMapping("/export")
    public ResponseEntity<ApiResponse<Void>> export() {
        return ResponseEntity.ok(ApiResponse.success("导出功能开发中", null));
    }

}
