package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 套餐 crud
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    JedisPool jedisPool;

    @Reference
    SetmealService setmealService;

    //图片文件上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
//        拿到文件真实名
        String originalFilename = imgFile.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + ext;
        try {
//            文件上传到七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
//将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, @RequestParam("checkgroupIds") Integer[] checkgroupIds) {
        try {
            setmealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);

    }

    @RequestMapping("/findPage")
//    @ResponseBody
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.findPage(queryPageBean);
        return pageResult;

    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
//            查询成功
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
//            失败
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }


    }

    @RequestMapping("/findCheckGroupIdsBysetmealId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {
        try {
//            查询成功
            List<Integer> list = setmealService.findCheckGroupIdsBysetmealId(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
//            失败
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {
        try {
            setmealService.edit(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setmealService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

}
