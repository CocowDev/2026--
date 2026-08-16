package com.hotel.booking.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.booking.dto.BookingDTO;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.entity.RoomType;
import com.hotel.booking.mapper.BookingMapper;
import com.hotel.booking.mapper.RoomTypeMapper;
import com.hotel.booking.vo.BookingVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    /**
     * 分页查询预订列表（联表 VO，含用户名/房型名等展示字段）
     */
    public IPage<BookingVO> getBookings(int page, int pageSize, String status, String keyword) {
        Page<BookingVO> pageRequest = new Page<>(page, pageSize);
        return bookingMapper.findByPageWithDetails(pageRequest, status, keyword);
    }

    /**
     * 查询预订详情（联表 VO）
     */
    public BookingVO getBookingById(Long id) {
        return bookingMapper.findByIdWithDetails(id);
    }

    /**
     * 创建客房预订：按房型单价 × 晚数计算总价（BigDecimal 精确计算）
     */
    public void createBooking(BookingDTO bookingDTO, Long userId) {
        RoomType roomType = roomTypeMapper.selectById(bookingDTO.getRoomTypeId());
        if (roomType == null) {
            throw new RuntimeException("房型不存在");
        }

        // 入住/离店日期：DTO 已是 LocalDate，直接计算晚数
        LocalDate checkIn = bookingDTO.getCheckInDate();
        LocalDate checkOut = bookingDTO.getCheckOutDate();
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);

        if (nights <= 0) {
            throw new RuntimeException("离店日期必须晚于入住日期");
        }

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomTypeId(bookingDTO.getRoomTypeId());
        booking.setType("room");
        booking.setGuestName(bookingDTO.getGuestName());
        booking.setGuestPhone(bookingDTO.getGuestPhone());
        booking.setGuestEmail(bookingDTO.getGuestEmail());
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setGuestCount(bookingDTO.getGuestCount());
        booking.setSpecialRequests(bookingDTO.getSpecialRequests());
        booking.setStatus("pending");
        // 总价 = 单价 × 晚数，BigDecimal 避免浮点误差
        booking.setTotalPrice(roomType.getPrice().multiply(BigDecimal.valueOf(nights)));
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

    /**
     * 更新预订：重新按房型单价 × 晚数计算总价
     */
    public void updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预订不存在");
        }
        RoomType roomType = roomTypeMapper.selectById(bookingDTO.getRoomTypeId());
        if (roomType == null) {
            throw new RuntimeException("房型不存在");
        }

        LocalDate checkIn = bookingDTO.getCheckInDate();
        LocalDate checkOut = bookingDTO.getCheckOutDate();
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            throw new RuntimeException("离店日期必须晚于入住日期");
        }

        booking.setRoomTypeId(bookingDTO.getRoomTypeId());
        booking.setGuestName(bookingDTO.getGuestName());
        booking.setGuestPhone(bookingDTO.getGuestPhone());
        booking.setGuestEmail(bookingDTO.getGuestEmail());
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setGuestCount(bookingDTO.getGuestCount());
        booking.setSpecialRequests(bookingDTO.getSpecialRequests());
        booking.setTotalPrice(roomType.getPrice().multiply(BigDecimal.valueOf(nights)));
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

    /**
     * 近期预订（仪表盘）：联表 VO，注意需加 LIMIT 防全表加载（P2 待办）
     */
    public List<BookingVO> getRecentBookings() {
        return bookingMapper.findAllWithDetails();
    }

    /**
     * 仪表盘统计：营收按 BigDecimal 精确汇总
     */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long total = bookingMapper.selectCount(null);
        long pending = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "pending"));
        long confirmed = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "confirmed"));
        long completed = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "completed"));
        
        // 今日新增预订数：createdAt 在今天 0 点之后
        long todayCount = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .ge(Booking::getCreatedAt, LocalDate.now().atStartOfDay()));

        // 营收 = 已确认 + 已完成订单总价之和（BigDecimal 精确累加）
        BigDecimal totalRevenue = bookingMapper.selectList(new LambdaQueryWrapper<Booking>()
                .in(Booking::getStatus, "confirmed", "completed"))
                .stream()
                .map(Booking::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        stats.put("totalBookings", total);
        stats.put("pendingBookings", pending);
        stats.put("confirmedBookings", confirmed);
        stats.put("completedBookings", completed);
        stats.put("todayCount", todayCount);
        stats.put("totalRevenue", totalRevenue);
        
        return stats;
    }

}
