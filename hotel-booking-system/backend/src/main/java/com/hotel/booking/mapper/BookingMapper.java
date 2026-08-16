package com.hotel.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.booking.entity.Booking;
import com.hotel.booking.vo.BookingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookingMapper extends BaseMapper<Booking> {

    /**
     * 查询全部预订（联表：用户名/手机号 + 房型名/单价/图片/描述）
     * 注意：别名 user_name/room_title 等由 map-underscore-to-camel-case 自动映射到 BookingVO
     * LIMIT 10：供仪表盘"近期预订"使用，避免全表加载拖慢响应（P2 优化）
     */
    @Select("SELECT b.*, u.username as user_name, u.phone as user_phone, " +
            "rt.title as room_title, rt.price as room_price, " +
            "rt.imageUrl as room_image_url, rt.description as room_description " +
            "FROM bookings b " +
            "LEFT JOIN users u ON b.userId = u.id " +
            "LEFT JOIN room_types rt ON b.roomTypeId = rt.id " +
            "ORDER BY b.createdAt DESC " +
            "LIMIT 10")
    List<BookingVO> findAllWithDetails();

    /**
     * 查询单条预订详情（联表）
     */
    @Select("SELECT b.*, u.username as user_name, u.phone as user_phone, " +
            "rt.title as room_title, rt.price as room_price, " +
            "rt.imageUrl as room_image_url, rt.description as room_description " +
            "FROM bookings b " +
            "LEFT JOIN users u ON b.userId = u.id " +
            "LEFT JOIN room_types rt ON b.roomTypeId = rt.id " +
            "WHERE b.id = #{id}")
    BookingVO findByIdWithDetails(Long id);

    /**
     * 分页查询预订（联表，支持状态/关键词筛选）
     */
    @Select("<script>" +
            "SELECT b.*, u.username as user_name, u.phone as user_phone, " +
            "rt.title as room_title, rt.price as room_price, " +
            "rt.imageUrl as room_image_url, rt.description as room_description " +
            "FROM bookings b " +
            "LEFT JOIN users u ON b.userId = u.id " +
            "LEFT JOIN room_types rt ON b.roomTypeId = rt.id " +
            "WHERE 1=1 " +
            "<if test='status != null and status != \"\"'>AND b.status = #{status}</if>" +
            "<if test='keyword != null and keyword != \"\"'>AND (b.guestName LIKE CONCAT('%', #{keyword}, '%') OR b.phone LIKE CONCAT('%', #{keyword}, '%'))</if>" +
            "ORDER BY b.createdAt DESC" +
            "</script>")
    IPage<BookingVO> findByPageWithDetails(Page<BookingVO> page, @Param("status") String status, @Param("keyword") String keyword);

}
