package com.yilan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yilan.domain.entity.User;

public interface UserService extends IService<User> {
    Long countByName(String name);

    Page<User> pageBySearch(int pageNum, int paseSize, String search);
}
