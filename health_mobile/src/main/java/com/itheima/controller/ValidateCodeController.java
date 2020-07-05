package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.util.SMSUtils;
import com.itheima.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * creste by itheima.itcast
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;

    //    /validateCode/send4Order.do?telephone=
    @RequestMapping("/send4Order")
    public Result send4Order(@RequestParam("telephone") String telephone) {
        try {
            String validateCode = ValidateCodeUtils.generateValidateCode4String(6);
            //发送预约短信验证码
            // SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            System.out.println("登录短信验证码:" + validateCode);

            //把验证码存到redis   15718886279-002
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, validateCode);

            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            //logger.error
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    //    /validateCode/send4Login.do?telephone=
    @RequestMapping("/send4Login")
    public Result send4Login(@RequestParam("telephone") String telephone) {
        try {
            String validateCode = ValidateCodeUtils.generateValidateCode4String(6);
            //发送预约短信验证码
            // SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode);
            System.out.println("预约短信验证码:" + validateCode);

            //把验证码存到redis   15718886279-001
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, validateCode);

            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            //logger.error
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }


}
