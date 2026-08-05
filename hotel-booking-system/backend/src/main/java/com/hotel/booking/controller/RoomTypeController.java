package com.hotel.booking.controller;

import com.hotel.booking.dto.ApiResponse;
import com.hotel.booking.entity.RoomType;
import com.hotel.booking.mapper.RoomTypeMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final RoomTypeMapper roomTypeMapper;

    public RoomTypeController(RoomTypeMapper roomTypeMapper) {
        this.roomTypeMapper = roomTypeMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomType>>> list() {
        List<RoomType> roomTypes = roomTypeMapper.selectList(null);
        return ResponseEntity.ok(ApiResponse.success(roomTypes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomType>> detail(@PathVariable Long id) {
        RoomType roomType = roomTypeMapper.selectById(id);
        if (roomType == null) {
            return ResponseEntity.ok(ApiResponse.notFound("房型不存在"));
        }
        return ResponseEntity.ok(ApiResponse.success(roomType));
    }

}
