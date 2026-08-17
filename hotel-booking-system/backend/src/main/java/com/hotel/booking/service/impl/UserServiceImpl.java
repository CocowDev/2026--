package com.hotel.booking.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotel.booking.entity.User;
import com.hotel.booking.mapper.UserMapper;
import com.hotel.booking.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public IPage<User> getUsers(int page, int pageSize) {
        Page<User> pageRequest = new Page<>(page, pageSize);
        return userMapper.selectPage(pageRequest, null);
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 更新用户：不允许通过此接口修改密码（保留原密码）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long id, User user) {
        User existing = userMapper.selectById(id);
        if (existing == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setId(id);
        user.setPassword(existing.getPassword());
        userMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        userMapper.deleteById(id);
    }

    @Override
    public long getActiveUsersCount() {
        return userMapper.selectCount(null);
    }

}
