package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import com.itheima.util.QiniuUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * creste by itheima.itcast
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    SetmealDao setmealDao;
    @Autowired
    JedisPool jedisPool;
    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //1 套餐表加数据
        setmealDao.add(setmeal);
        //2 关系表加数据
        reAssociation(setmeal.getId(), checkgroupIds);

        //3图片名字放入 dbrediskey
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());

        //生成所有静态化页面方法
        generateMobileStaticHtml();

    }

    //生成所有静态化页面方法
    private void generateMobileStaticHtml() {
        List<Setmeal> checkgroups = setmealDao.getAll();
        //1 套餐列表静态化页面
        generateMobileSetmealListHtml(checkgroups);
        //2 套餐详情页静态化页面
        generateMobileSetmealDetailHtml(checkgroups);
    }

    //套餐列表静态化页面
    public void generateMobileSetmealListHtml(List<Setmeal> list) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("setmealList", list);
        generateHtml("mobile_setmeal.ftl", dataMap, "m_setmeal.html");
    }

    //套餐详情页静态化页面 多个
    public void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("setmeal", setmealDao.findById(setmeal.getId()));
            List<Integer> checkGroupIdsBysetmealId = setmealDao.findCheckGroupIdsBysetmealId(setmeal.getId());
            System.out.println(checkGroupIdsBysetmealId);
            dataMap.put("checkgroupIds",checkGroupIdsBysetmealId);
            generateHtml("mobile_setmeal_detail.ftl", dataMap, "setmeal_detail_" + setmeal.getId() + ".html");
        }
    }

    @Autowired
    FreeMarkerConfigurer freemarkerConfig;
    @Value("${out_put_path}")
    String outPutPath;

    //通用生成静态化页面方法
    public void generateHtml(String templateName, Map<String, Object> dataMap, String htmlPageName) {
        try {
            //1拿模板
            Configuration configuration = freemarkerConfig.getConfiguration();
            Template template = configuration.getTemplate(templateName);
            //2拿数据 业务逻辑

            //3生成文本
            FileWriter fileWriter = new FileWriter(new File(outPutPath + htmlPageName));
            template.process(dataMap, fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //修改套餐 generateMobileStaticHtml();

    //删除套餐 generateMobileStaticHtml();


    private void reAssociation(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("setmeal_id", setmealId);
            map.put("checkgroup_id", checkgroupId);
            //新增逻辑
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //PageHelper分页 ThreadLocal
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        Page<Setmeal> page = setmealDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<Setmeal> getAll() {
        return setmealDao.getAll();
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }


    @Override
    public void delete(Integer id) {
        //查询检查套餐
        Setmeal setmeal = findById(id);
//        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        String img = setmeal.getImg();
        QiniuUtils.deleteFileFromQiniu(img);

        //先删除中间表
        setmealDao.deleteAssociation(id);

        //再删除检查套餐
        setmealDao.deleteById(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        //        1 修改基本检查组
        setmealDao.edit(setmeal);
        //2.删除检查组
        setmealDao.deleteAssociation(setmeal.getId());
        //3.重新添加检查组
        setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);

        //在编辑中,生成静态页面的方法
        generateMobileStaticHtml();
    }

    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            for (Integer checkgroupId : checkgroupIds) {
                HashMap<String, Integer> map = new HashMap<>();
                map.put("setmeal_id", setmealId);
                map.put("checkgroup_id", checkgroupId);
                setmealDao.setSetmealAndCheckGroup(map);

            }
        }
    }


    @Override
    public List<Integer> findCheckGroupIdsBysetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBysetmealId(id);
    }

    @Override
    public List<Setmeal> getAllSetmeal() {
        return setmealDao.getAllSetmeal();
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
