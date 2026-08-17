package com.hotel.booking.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotel.booking.dto.BookingDTO;
import com.hotel.booking.dto.ServiceBookingDTO;
import com.hotel.booking.vo.BookingVO;

import java.util.List;
import java.util.Map;

/**
 * 预订服务接口
 * 覆盖：客房预订增删改查、状态流转、用户取消、服务预订、仪表盘统计
 */
public interface BookingService {

    /** 分页查询预订列表；userId 为 null 表示管理员查询全部，否则仅本人订单 */
    IPage<BookingVO> getBookings(int page, int pageSize, String status, String keyword, Long userId);

    /** 查询预订详情（餐饮预订含所选菜品） */
    BookingVO getBookingById(Long id);

    /** 创建客房预订（总价 = 单价 × 晚数，BigDecimal 精确计算） */
    void createBooking(BookingDTO bookingDTO, Long userId);

    /** 更新预订状态 */
    void updateBookingStatus(Long id, String status);

    /** 更新预订（重算总价） */
    void updateBooking(Long id, BookingDTO bookingDTO);

    /** 删除预订 */
    void deleteBooking(Long id);

    /** 用户取消自己的待处理订单（个人中心） */
    void cancelBooking(Long id, Long userId);

    /** 创建服务预订（type='service'） */
    void createServiceBooking(ServiceBookingDTO dto, Long userId);

    /** 近期预订（仪表盘，LIMIT 10） */
    List<BookingVO> getRecentBookings();

    /** 仪表盘统计（含 todayCount 与 BigDecimal 营收汇总） */
    Map<String, Object> getStats();

}
