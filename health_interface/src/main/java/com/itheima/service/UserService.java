package com.itheima.service;

import com.itheima.pojo.User;

/**
 * 对用户做一些 增删改查
 */
public interface UserService {
    User findByUsername(String username);

}
