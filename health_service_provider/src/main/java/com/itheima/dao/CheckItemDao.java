package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> selectByCondition(@Param("queryString") String queryString);

    void deletebyId(Integer id);

    long findCountByCheckItemId(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
