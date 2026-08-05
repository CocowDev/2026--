package com.hotel.booking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.booking.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT id, username AS name, phone, email, password, createdAt, updatedAt FROM users WHERE username = #{keyword} OR phone = #{keyword} OR email = #{keyword}")
    User findByPhoneOrEmail(String keyword);

}
