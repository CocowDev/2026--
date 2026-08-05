package com.hotel.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.booking.entity.Restaurant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RestaurantMapper extends BaseMapper<Restaurant> {

    // 餐饮预订：插入 bookings 表（type='restaurant'）
    // 注意：bookings 表 phone 列为 NOT NULL，需从用户信息补充
    @Insert("INSERT INTO bookings (userId, restaurantId, type, bookingDate, bookingTime, guests, " +
            "guestName, phone, email, specialRequests, totalPrice, status, createdAt, updatedAt) " +
            "VALUES (#{userId}, #{restaurantId}, 'restaurant', #{reservationDate}, #{reservationTime}, " +
            "#{numberOfGuests}, #{guestName}, #{phone}, #{email}, #{specialRequests}, 0, 'pending', NOW(), NOW())")
    int insertRestaurantReservation(Map<String, Object> data);

}
