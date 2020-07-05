package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(HashMap<String, Integer> map);

    Page<Setmeal> selectByCondition(@Param("queryString") String queryString);

    List<Setmeal> getAll();

    Setmeal findById(int id);

    void deleteAssociation(Integer id);

    void deleteById(Integer id);

    void edit(Setmeal setmeal);

    List<Integer> findCheckGroupIdsBysetmealId(Integer id);

    List<Setmeal> getAllSetmeal();

    List<Map<String, Object>> findSetmealCount();

}
