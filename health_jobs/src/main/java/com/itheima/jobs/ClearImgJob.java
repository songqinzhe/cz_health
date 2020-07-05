package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * creste by itheima.itcast
 */
public class ClearImgJob {
    @Autowired
    JedisPool jedisPool;

    //业务逻辑
    public void clearImg(){
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String filename : set) {
            //七牛云删除 垃圾图片
            QiniuUtils.deleteFileFromQiniu(filename);
            //从redis 删除垃圾图片文件名     string:SETMEAL_PIC_RESOURCES    value set[123.jpg,456.jpg,789.jpg]
            //jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
        }
        jedisPool.getResource().del(RedisConstant.SETMEAL_PIC_RESOURCES);
        jedisPool.getResource().del(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //好处 1节约内存空间 2节省删除 删redis逻辑
        //坏处 1编辑 判断
    }
}
