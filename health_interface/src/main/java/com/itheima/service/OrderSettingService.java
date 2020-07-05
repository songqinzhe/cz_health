package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;

/**
 * creste by itheima.itcast
 */
public interface OrderSettingService {
    void add(List<OrderSetting> list);

    List<OrderSetting> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
