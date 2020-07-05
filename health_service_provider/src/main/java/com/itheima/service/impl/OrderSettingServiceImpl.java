package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * creste by itheima.itcast
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService{
    @Autowired
    OrderSettingDao orderSettingDao;

    @Override
    public void add(List<OrderSetting> list) {
        //判断
        if(list!=null&&list.size()>0){
            for (OrderSetting orderSetting : list) {
                //业务逻辑
                //1根据时间判断 有没有
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(countByOrderDate>0){
                    //2有 修改
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //3没有 新增
                    orderSettingDao.add(orderSetting);
                }
            }
        }

    }

    @Override
    public List<OrderSetting> getOrderSettingByMonth(String date) { //2020-05
        Map<String, String> map=new HashMap<>();
        map.put("begin", date+"-01");
        map.put("end", date+"-31");
        List<OrderSetting> list=orderSettingDao.getOrderSettingByMonth(map);
        return list;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //1根据时间判断 有没有
        long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(countByOrderDate>0){
            //2有 修改
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //3没有 新增
            orderSettingDao.add(orderSetting);
        }
    }
}
