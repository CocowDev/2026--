package com.hotel.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.booking.dto.BookingDTO;
import com.hotel.booking.dto.ServiceBookingDTO;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.entity.RoomType;
import com.hotel.booking.mapper.BookingDishMapper;
import com.hotel.booking.mapper.BookingMapper;
import com.hotel.booking.mapper.RoomTypeMapper;
import com.hotel.booking.service.BookingService;
import com.hotel.booking.vo.BookingVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 预订服务实现：客房预订、服务预订、状态流转、取消、仪表盘统计
 */
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final RoomTypeMapper roomTypeMapper;
    private final BookingDishMapper bookingDishMapper;

    public BookingServiceImpl(BookingMapper bookingMapper, RoomTypeMapper roomTypeMapper,
                              BookingDishMapper bookingDishMapper) {
        this.bookingMapper = bookingMapper;
        this.roomTypeMapper = roomTypeMapper;
        this.bookingDishMapper = bookingDishMapper;
    }

    @Override
    public IPage<BookingVO> getBookings(int page, int pageSize, String status, String keyword, Long userId) {
        Page<BookingVO> pageRequest = new Page<>(page, pageSize);
        return bookingMapper.findByPageWithDetails(pageRequest, status, keyword, userId);
    }

    @Override
    public BookingVO getBookingById(Long id) {
        BookingVO vo = bookingMapper.findByIdWithDetails(id);
        if (vo != null && "restaurant".equals(vo.getType())) {
            vo.setDishes(bookingDishMapper.findByBookingId(id));
        }
        return vo;
    }

    /**
     * 创建客房预订：总价 = 单价 × 晚数（BigDecimal 精确计算），事务保证原子性
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBooking(BookingDTO bookingDTO, Long userId) {
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
        booking.setTotalPrice(roomType.getPrice().multiply(BigDecimal.valueOf(nights)));
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        bookingMapper.insert(booking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Override
    @Transactional(rollbackFor = Exception.class)
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBooking(Long id) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预订不存在");
        }
        
        bookingMapper.deleteById(id);
    }

    /**
     * 用户取消自己的预订（个人中心）：仅限本人且订单为待处理状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelBooking(Long id, Long userId) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new RuntimeException("预订不存在");
        }
        if (!Objects.equals(booking.getUserId(), userId)) {
            throw new RuntimeException("无权取消该预订");
        }
        if (!"pending".equals(booking.getStatus())) {
            throw new RuntimeException("仅待处理状态的订单可取消");
        }
        booking.setStatus("cancelled");
        booking.setUpdatedAt(LocalDateTime.now());
        bookingMapper.updateById(booking);
    }

    /**
     * 创建服务预订（SPA/健身/泳池等）：写入 bookings 表 type='service'
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createServiceBooking(ServiceBookingDTO dto, Long userId) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setType("service");
        booking.setGuestName(dto.getGuestName());
        booking.setGuestPhone(dto.getGuestPhone());
        booking.setCheckInDate(dto.getServiceDate());
        booking.setSpecialRequests("服务：" + dto.getServiceName()
                + "，人数：" + dto.getGuests()
                + (dto.getRemark() == null || dto.getRemark().isBlank() ? "" : "，备注：" + dto.getRemark()));
        booking.setGuestCount(dto.getGuests());
        booking.setStatus("pending");
        booking.setTotalPrice(dto.getPrice());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());
        bookingMapper.insert(booking);
    }

    @Override
    public List<BookingVO> getRecentBookings() {
        return bookingMapper.findAllWithDetails();
    }

    /**
     * 仪表盘统计：营收按 BigDecimal 精确汇总
     */
    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long total = bookingMapper.selectCount(null);
        long pending = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "pending"));
        long confirmed = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "confirmed"));
        long completed = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .eq(Booking::getStatus, "completed"));

        long todayCount = bookingMapper.selectCount(new LambdaQueryWrapper<Booking>()
                .ge(Booking::getCreatedAt, LocalDate.now().atStartOfDay()));

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
