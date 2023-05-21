package com.yilan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yilan.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
