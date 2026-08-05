package com.hotel.booking.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.booking.dto.BookingDTO;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.entity.RoomType;
import com.hotel.booking.mapper.BookingMapper;
import com.hotel.booking.mapper.RoomTypeMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {

    private final BookingMapper bookingMapper;
    private final RoomTypeMapper roomTypeMapper;

    public BookingService(BookingMapper bookingMapper, RoomTypeMapper roomTypeMapper) {
        this.bookingMapper = bookingMapper;
        this.roomTypeMapper = roomTypeMapper;
    }

    public IPage<Booking> getBookings(int page, int pageSize, String status, String keyword) {
        Page<Booking> pageRequest = new Page<>(page, pageSize);
        return bookingMapper.findByPageWithDetails(pageRequest, status, keyword);
    }

    public Booking getBookingById(Long id) {
        return bookingMapper.findByIdWithDetails(id);
    }

    public void createBooking(BookingDTO bookingDTO, Long userId) {
        RoomType roomType = roomTypeMapper.selectById(bookingDTO.getRoomTypeId());
        if (roomType == null) {
            throw new RuntimeException("房型不存在");
        }
        
        LocalDate checkIn = LocalDate.parse(bookingDTO.getCheckInDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate checkOut = LocalDate.parse(bookingDTO.getCheckOutDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        
        if (nights <= 0) {
            throw new RuntimeException("离店日期必须晚于入住日期");
        }
        
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomTypeId(bookingDTO.getRoomTypeId());
        booking.setGuestName(bookingDTO.getGuestName());
        booking.setGuestPhone(bookingDTO.getGuestPhone());
        booking.setGuestEmail(bookingDTO.getGuestEmail());
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setGuestCount(bookingDTO.getGuestCount());
        booking.setSpecialRequests(bookingDTO.getSpecialRequests());
        booking.setStatus("pending");
        booking.setTotalPrice(roomType.getPrice() * nights);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        
        bookingMapper.insert(booking);
    }

    public void updateBookingStatus(Long id, String status) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预订不存在");
        }

        booking.setStatus(status);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingMapper.updateById(booking);
    }

    public void updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预订不存在");
        }
        RoomType roomType = roomTypeMapper.selectById(bookingDTO.getRoomTypeId());
        if (roomType == null) {
            throw new RuntimeException("房型不存在");
        }

        LocalDate checkIn = LocalDate.parse(bookingDTO.getCheckInDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate checkOut = LocalDate.parse(bookingDTO.getCheckOutDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            throw new RuntimeException("离店日期必须晚于入住日期");
        }

        booking.setRoomTypeId(bookingDTO.getRoomTypeId());
        booking.setGuestName(bookingDTO.getGuestName());
        booking.setGuestPhone(bookingDTO.getGuestPhone());
        booking.setGuestEmail(bookingDTO.getGuestEmail());
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setGuestCount(bookingDTO.getGuestCount());
        booking.setSpecialRequests(bookingDTO.getSpecialRequests());
        booking.setTotalPrice(roomType.getPrice() * nights);
        booking.setUpdatedAt(LocalDateTime.now());

        bookingMapper.updateById(booking);
    }

    public void deleteBooking(Long id) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预订不存在");
        }
        
        bookingMapper.deleteById(id);
    }

    public List<Booking> getRecentBookings() {
        return bookingMapper.findAllWithDetails();
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long total = bookingMapper.selectCount(null);
        long pending = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "pending"));
        long confirmed = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "confirmed"));
        long completed = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "completed"));
        
        Double totalRevenue = bookingMapper.selectList(new LambdaQueryWrapper<Booking>()
                .in(Booking::getStatus, "confirmed", "completed"))
                .stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();
        
        stats.put("totalBookings", total);
        stats.put("pendingBookings", pending);
        stats.put("confirmedBookings", confirmed);
        stats.put("completedBookings", completed);
        stats.put("totalRevenue", totalRevenue);
        
        return stats;
    }

}
