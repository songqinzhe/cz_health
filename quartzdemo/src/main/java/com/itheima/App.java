package com.itheima;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 启动类
 */
public class App {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-jobs.xml");
    }


}
