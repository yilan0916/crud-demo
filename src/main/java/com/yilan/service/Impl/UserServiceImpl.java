package com.yilan.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yilan.domain.entity.User;
import com.yilan.mapper.UserMapper;
import com.yilan.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public Long countByName(String name) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getName, name);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public Page<User> pageBySearch(int pageNum, int paseSize, String search) {
        Page<User> page = new Page<>(pageNum, paseSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(search), User::getId, search)
                .or().like(StringUtils.isNotBlank(search), User::getName, search)
                .or().like(StringUtils.isNotBlank(search), User::getAge, search)
                .or().like(StringUtils.isNotBlank(search), User::getEmail, search);
        return userMapper.selectPage(page, queryWrapper);
    }
}
