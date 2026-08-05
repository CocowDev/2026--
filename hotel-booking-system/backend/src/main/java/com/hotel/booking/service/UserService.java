package com.hotel.booking.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.booking.entity.User;
import com.hotel.booking.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public IPage<User> getUsers(int page, int pageSize) {
        Page<User> pageRequest = new Page<>(page, pageSize);
        return userMapper.selectPage(pageRequest, null);
    }

    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    public void updateUser(Long id, User user) {
        User existing = userMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setId(id);
        // 不允许通过此接口修改密码；保留原密码
        user.setPassword(existing.getPassword());
        userMapper.updateById(user);
    }

    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        userMapper.deleteById(id);
    }

    public long getActiveUsersCount() {
        return userMapper.selectCount(null);
    }

}
