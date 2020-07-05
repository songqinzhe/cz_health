package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.dao.SetmealDao;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckItemService;
import com.itheima.service.OrderService;
import com.itheima.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * creste by itheima.itcast
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Integer order(Map<String, String> map) throws Exception {

        // 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = map.get("orderDate");
        // Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting == null) {
            return null;
        }

        // 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber(); //可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number) {
            return null;
        }

        // 3、检查用户是否重复预约（同一个用户member_id在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);
        if (member != null) {
            Integer memberId = member.getId();
            Order order = new Order();
            order.setMemberId(memberId);
            order.setOrderDate(DateUtils.parseString2Date(orderDate));
            order.setSetmealId(Integer.parseInt(map.get("setmealId")));
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0) {
                return null;
            }
        } else {
            // 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            //作业3 前端弹窗 让用户完善信息
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }


        // 5、预约成功，更新当日的已预约人数
        //5.1order 新增
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt(map.get("setmealId")));
        orderDao.add(order);
        //5.2更新当日的已预约人数
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);

        return order.getId();
    }

    @Autowired
    SetmealDao setmealDao;
    @Override
    public Map<String, String> findById(Integer id) {
        // Map<String, String> resultMap = new HashMap<>();
        // <p>体检人：{{orderInfo.member}}</p>
        // <p>体检套餐：{{orderInfo.setmeal}}</p>
        // <p>体检日期：{{orderInfo.orderDate}}</p>
        // <p>预约类型：{{orderInfo.orderType}}</p>
        //方式一：要啥查啥
        // Order order = new Order();
        // order.setId(id);
        // List<Order> list = orderDao.findByCondition(order);
        // Order order1 = list.get(0);
        //
        // try {
        //     resultMap.put("orderDate",DateUtils.parseDate2String(order1.getOrderDate())); // 2020/05/26 00:00:00 GMT+8
        //     resultMap.put("orderType",order1.getOrderType());
        //
        //     Integer memberId = order1.getMemberId();
        //     Member member = memberDao.findById(memberId);
        //     resultMap.put("member",member.getName());
        //
        //     Integer setmealId = order1.getSetmealId();
        //     Setmeal setmeal = setmealDao.findById(setmealId);
        //     resultMap.put("setmeal",setmeal.getName());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // return resultMap;

        //方式一：一下全查出来

        Map map = orderDao.findById4Detail(id);
        try {
            Date orderDate = (Date) map.get("orderDate");
            String dateStr = DateUtils.parseDate2String(orderDate);
            map.put("orderDate",dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
