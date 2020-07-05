package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * creste by itheima.itcast
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    SetmealService setmealService;

    @RequestMapping("/getAllSetmeal")
    public Result getAllSetmeal() {
        try {
            List<Setmeal> list = setmealService.getAllSetmeal();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            //logger.error(e)
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") int id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            //logger.error(e)
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }
}
