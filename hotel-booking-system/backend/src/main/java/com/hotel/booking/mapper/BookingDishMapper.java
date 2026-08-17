package com.hotel.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.booking.entity.BookingDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookingDishMapper extends BaseMapper<BookingDish> {

    /** 按预订 ID 查询所选菜品 */
    @Select("SELECT * FROM booking_dishes WHERE bookingId = #{bookingId}")
    List<BookingDish> findByBookingId(Long bookingId);

}
