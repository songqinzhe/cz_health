package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface SetmealService {

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult findPage(QueryPageBean queryPageBean);

    Setmeal findById(int id);

    List<Integer> findCheckGroupIdsBysetmealId(Integer id);

    void delete(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> getAll();

    List<Setmeal> getAllSetmeal();

    List<Map<String, Object>> findSetmealCount();

}
