package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.util.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

/**
 * creste by itheima.itcast
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    JedisPool jedisPool;
    @Reference
    OrderService orderService;

//    /order/submit.do
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map<String, String> map) {
        //1校验验证码
        //1.1redis取出验证码
        String telephone = map.get("telephone");
        String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //1.2map验证码对比
        String validateCode = map.get("validateCode");
        if (redisValidateCode == null || validateCode == null || !redisValidateCode.equals(validateCode)) {
            return new Result(false, MessageConstant.ORDER_FAIL);
        }

        //2对比通过  service 存入数据库         枚举 enum作业2
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        Integer id = null;
        try {
            id = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
        if (id == null) {
            return new Result(false, MessageConstant.ORDER_FAIL);
        }

        //3预约成功，发送短信通知
        try {
            String orderDate = map.get("orderDate");
            // SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            System.out.println("发送了预约成功短信");
        } catch (Exception e) {
            //短信重发 3次 作业1
        }

        return new Result(true, MessageConstant.ORDER_SUCCESS, id);
    }


    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") Integer id) {
        try {
            Map<String, String> map = orderService.findById(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
