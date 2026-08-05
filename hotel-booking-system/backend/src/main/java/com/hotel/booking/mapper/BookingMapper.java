package com.hotel.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.booking.entity.Booking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookingMapper extends BaseMapper<Booking> {

    @Select("SELECT b.*, u.username as user_name, u.phone as user_phone, rt.title as room_title " +
            "FROM bookings b " +
            "LEFT JOIN users u ON b.userId = u.id " +
            "LEFT JOIN room_types rt ON b.roomTypeId = rt.id " +
            "ORDER BY b.createdAt DESC")
    List<Booking> findAllWithDetails();

    @Select("SELECT b.*, u.username as user_name, u.phone as user_phone, rt.title as room_title " +
            "FROM bookings b " +
            "LEFT JOIN users u ON b.userId = u.id " +
            "LEFT JOIN room_types rt ON b.roomTypeId = rt.id " +
            "WHERE b.id = #{id}")
    Booking findByIdWithDetails(Long id);

    @Select("<script>" +
            "SELECT b.*, u.username as user_name, u.phone as user_phone, rt.title as room_title " +
            "FROM bookings b " +
            "LEFT JOIN users u ON b.userId = u.id " +
            "LEFT JOIN room_types rt ON b.roomTypeId = rt.id " +
            "WHERE 1=1 " +
            "<if test='status != null and status != \"\"'>AND b.status = #{status}</if>" +
            "<if test='keyword != null and keyword != \"\"'>AND (b.guestName LIKE CONCAT('%', #{keyword}, '%') OR b.phone LIKE CONCAT('%', #{keyword}, '%'))</if>" +
            "ORDER BY b.createdAt DESC" +
            "</script>")
    IPage<Booking> findByPageWithDetails(Page<Booking> page, @Param("status") String status, @Param("keyword") String keyword);

}
