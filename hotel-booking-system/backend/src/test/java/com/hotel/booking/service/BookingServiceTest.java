package com.hotel.booking.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.booking.dto.BookingDTO;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.entity.RoomType;
import com.hotel.booking.mapper.BookingMapper;
import com.hotel.booking.mapper.RoomTypeMapper;
import com.hotel.booking.vo.BookingVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * BookingService 单元测试
 * 覆盖：创建/更新预订（含 BigDecimal 总价精确计算、日期校验）、状态更新、删除、
 * 仪表盘统计（含 todayCount 与营收汇总）、联表查询委托
 */
@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private RoomTypeMapper roomTypeMapper;

    @InjectMocks
    private BookingService bookingService;

    /** 构造合法预订请求：2026-09-01 入住，2026-09-03 离店（2 晚） */
    private BookingDTO buildBookingDTO() {
        BookingDTO dto = new BookingDTO();
        dto.setRoomTypeId(1L);
        dto.setGuestName("张三");
        dto.setGuestPhone("13800000000");
        dto.setGuestEmail("zs@test.com");
        dto.setCheckInDate(LocalDate.of(2026, 9, 1));
        dto.setCheckOutDate(LocalDate.of(2026, 9, 3));
        dto.setGuestCount(2);
        dto.setSpecialRequests("无");
        return dto;
    }

    /** 构造房型：单价 500.00 */
    private RoomType buildRoomType() {
        RoomType roomType = new RoomType();
        roomType.setId(1L);
        roomType.setTitle("标准间");
        roomType.setPrice(new BigDecimal("500.00"));
        return roomType;
    }

    @Test
    void createBooking_成功_总价精确计算并写入正确字段() {
        when(roomTypeMapper.selectById(1L)).thenReturn(buildRoomType());

        bookingService.createBooking(buildBookingDTO(), 10L);

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingMapper).insert(captor.capture());
        Booking saved = captor.getValue();
        assertEquals(10L, saved.getUserId());
        assertEquals("room", saved.getType());
        assertEquals("pending", saved.getStatus());
        // 500.00 × 2 晚 = 1000.00（BigDecimal 精确计算，无浮点误差）
        assertEquals(new BigDecimal("1000.00"), saved.getTotalPrice());
        assertEquals(LocalDate.of(2026, 9, 1), saved.getCheckInDate());
        assertEquals(LocalDate.of(2026, 9, 3), saved.getCheckOutDate());
        assertEquals(2, saved.getGuestCount());
    }

    @Test
    void createBooking_房型不存在_抛出异常且不入库() {
        when(roomTypeMapper.selectById(1L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(buildBookingDTO(), 10L));
        verify(bookingMapper, never()).insert(any(Booking.class));
    }

    @Test
    void createBooking_离店日期不晚于入住_抛出异常() {
        when(roomTypeMapper.selectById(1L)).thenReturn(buildRoomType());
        BookingDTO dto = buildBookingDTO();
        dto.setCheckOutDate(LocalDate.of(2026, 9, 1)); // 同日入住离店

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(dto, 10L));
        verify(bookingMapper, never()).insert(any(Booking.class));
    }

    @Test
    void updateBooking_成功_重算总价() {
        Booking existing = new Booking();
        existing.setId(5L);
        when(bookingMapper.selectById(5L)).thenReturn(existing);
        when(roomTypeMapper.selectById(1L)).thenReturn(buildRoomType());

        bookingService.updateBooking(5L, buildBookingDTO());

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingMapper).updateById(captor.capture());
        assertEquals(new BigDecimal("1000.00"), captor.getValue().getTotalPrice());
        assertEquals("张三", captor.getValue().getGuestName());
    }

    @Test
    void updateBooking_预订不存在_抛出异常() {
        when(bookingMapper.selectById(5L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> bookingService.updateBooking(5L, buildBookingDTO()));
        verify(bookingMapper, never()).updateById(any(Booking.class));
    }

    @Test
    void updateBookingStatus_成功_更新状态() {
        Booking existing = new Booking();
        existing.setId(5L);
        existing.setStatus("pending");
        when(bookingMapper.selectById(5L)).thenReturn(existing);

        bookingService.updateBookingStatus(5L, "confirmed");

        assertEquals("confirmed", existing.getStatus());
        verify(bookingMapper).updateById(existing);
    }

    @Test
    void updateBookingStatus_预订不存在_抛出异常() {
        when(bookingMapper.selectById(5L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> bookingService.updateBookingStatus(5L, "confirmed"));
    }

    @Test
    void deleteBooking_成功_删除对应记录() {
        Booking existing = new Booking();
        existing.setId(5L);
        when(bookingMapper.selectById(5L)).thenReturn(existing);

        bookingService.deleteBooking(5L);

        verify(bookingMapper).deleteById(5L);
    }

    @Test
    void deleteBooking_预订不存在_抛出异常() {
        when(bookingMapper.selectById(5L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> bookingService.deleteBooking(5L));
    }

    @Test
    void getStats_统计各状态数量与营收汇总() {
        Booking confirmed = new Booking();
        confirmed.setTotalPrice(new BigDecimal("100.00"));
        Booking completed = new Booking();
        completed.setTotalPrice(new BigDecimal("200.50"));
        // selectCount 调用顺序：total/pending/confirmed/completed/todayCount
        when(bookingMapper.selectCount(any())).thenReturn(10L, 3L, 2L, 1L, 4L);
        when(bookingMapper.selectList(any())).thenReturn(List.of(confirmed, completed));

        Map<String, Object> stats = bookingService.getStats();

        assertEquals(10L, stats.get("totalBookings"));
        assertEquals(3L, stats.get("pendingBookings"));
        assertEquals(2L, stats.get("confirmedBookings"));
        assertEquals(1L, stats.get("completedBookings"));
        assertEquals(4L, stats.get("todayCount"));
        // 营收 = 100.00 + 200.50（BigDecimal 精确汇总）
        assertEquals(new BigDecimal("300.50"), stats.get("totalRevenue"));
    }

    @Test
    void getBookings_委托分页联表查询() {
        IPage<BookingVO> page = new Page<>();
        when(bookingMapper.findByPageWithDetails(any(), any(), any())).thenReturn(page);

        assertEquals(page, bookingService.getBookings(1, 10, "pending", "张"));
        verify(bookingMapper).findByPageWithDetails(any(), eq("pending"), eq("张"));
    }

    @Test
    void getBookingById_委托联表详情查询() {
        BookingVO vo = new BookingVO();
        vo.setId(1L);
        when(bookingMapper.findByIdWithDetails(1L)).thenReturn(vo);

        assertEquals(vo, bookingService.getBookingById(1L));
    }

    @Test
    void getRecentBookings_委托联表查询() {
        List<BookingVO> list = List.of(new BookingVO());
        when(bookingMapper.findAllWithDetails()).thenReturn(list);

        assertEquals(list, bookingService.getRecentBookings());
    }
}
