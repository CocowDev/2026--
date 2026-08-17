package com.hotel.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.booking.entity.Restaurant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

import java.util.Map;

@Mapper
public interface RestaurantMapper extends BaseMapper<Restaurant> {

    /**
     * 餐饮预订：插入 bookings 表（type='restaurant'），room_type_id 置空
     * 注意：列名必须与真实库驼峰列名一致（userId/guestName/bookingDate 等）
     * useGeneratedKeys：回填自增主键到 Map 的 "id" 键，供后续写入预订-菜品关联表
     */
    @Insert("INSERT INTO bookings (userId, roomTypeId, type, restaurantId, bookingDate, bookingTime, " +
            "guestName, phone, email, guests, specialRequests, totalPrice, status, createdAt, updatedAt) " +
            "VALUES (#{userId}, NULL, 'restaurant', #{restaurantId}, #{reservationDate}, #{reservationTime}, " +
            "#{guestName}, #{phone}, #{email}, #{numberOfGuests}, #{specialRequests}, 0, 'pending', NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertRestaurantReservation(Map<String, Object> data);

}
