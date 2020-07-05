package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

public interface OrderService {
    Integer order(Map<String,String> map) throws Exception;

    Map<String, String> findById(Integer id);
}
