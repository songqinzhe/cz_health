package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1checkgroup 表填数据
        checkGroupDao.add(checkGroup);

        //2关联表 填数据
        reAssociation(checkGroup.getId(), checkitemIds);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //PageHelper分页 ThreadLocal
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    //codereview
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //1检查组基本信息
        checkGroupDao.edit(checkGroup);
        //2清理检查项检查组关联 关系
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //3重新建立关系
        reAssociation(checkGroup.getId(), checkitemIds);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    public void reAssociation(Integer checkgrouid, Integer[] checkitemIds) {
        //3重新建立关系
        for (Integer checkitemId : checkitemIds) {
            //每一条数据 插入到关联表里
            Map<String, Integer> map = new HashMap<>();
            map.put("checkgrouid", checkgrouid);
            map.put("checkitemid", checkitemId);
            //新增逻辑
            checkGroupDao.setCheckGroupAndChenckItem(map);
        }
    }


    @Override
    public void delete(Integer id) {
        long count = checkGroupDao.findCheckGroupAndSetmealById(id);
        if (count > 0) {
//            System.out.println("删除检查组失败,当前检查组被引用");
            throw new RuntimeException("当前检查组被引用，不能删除");
        } else {
            checkGroupDao.deleteAssociation(id);
            checkGroupDao.deleteGroupById(id);
        }
    }
}
