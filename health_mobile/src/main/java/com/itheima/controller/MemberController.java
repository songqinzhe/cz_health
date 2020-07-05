package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
 * <p>
 * 2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
 * <p>
 * 3、向客户端写入Cookie，内容为用户手机号
 * <p>
 * 4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;


    //    /member/check.do
    //使用手机号和验证码登录
    @RequestMapping("/check")
    public Result login(HttpServletResponse response, @RequestBody Map<String, String> map) {
// * 1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if (validateCode == null || redisCode == null || !validateCode.equals(redisCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //验证码输入正确
        //判断当前用户是否为会员
        Member member = memberService.findByTelephone(telephone);
        if (member == null) {
            //当前用户不是会员，自动完成注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setPassword("1112");
            member.setRegTime(new Date());
            memberService.add(member);
        }

        //登录成功
        //3.写入Cookie，跟踪用户
        Cookie cookie = new Cookie("login_member_telephone", telephone);
        cookie.setPath("/");//路径 为啥是 /  ?
//            cookie.setMaxAge(-1);//浏览器关乐就没了
        cookie.setMaxAge(60 * 60 * 24 * 30);//有效期30天
        response.addCookie(cookie);
        //保存会员信息到Redis中
        String json = JSON.toJSONString(member);
        jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_MEMBER, 60 * 30, json);
// * 2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
// * <p>
// * 3、向客户端写入Cookie，内容为用户手机号
// * <p>
// * 4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟

        return new Result(true, MessageConstant.LOGIN_SUCCESS);

    }
}
