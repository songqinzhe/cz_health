package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //PageHelper分页 ThreadLocal
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        Page<CheckItem> page = checkItemDao.selectByCondition(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteById(Integer id) {
        //检查项的id 关联表里查询 有没有检查组引用检查项
        long countByCheckItemId = checkItemDao.findCountByCheckItemId(id);
        if (countByCheckItemId > 0) {
            throw new RuntimeException("查组引用检查项");
        }

        checkItemDao.deletebyId(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem = checkItemDao.findById(id);
        return checkItem;
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
