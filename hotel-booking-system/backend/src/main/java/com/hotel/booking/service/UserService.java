package com.hotel.booking.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hotel.booking.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /** 分页查询用户列表 */
    IPage<User> getUsers(int page, int pageSize);

    /** 查询用户详情 */
    User getUserById(Long id);

    /** 更新用户（保留原密码，禁止通过此接口修改密码） */
    void updateUser(Long id, User user);

    /** 删除用户 */
    void deleteUser(Long id);

    /** 活跃用户数（注册总数） */
    long getActiveUsersCount();

}
