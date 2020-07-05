package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
   void add(CheckGroup checkGroup);

   void setCheckGroupAndChenckItem(Map map);

    Page<CheckGroup> selectByCondition(@Param("queryString") String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssociation(Integer id);

    List<CheckGroup> findAll();

    long findCheckGroupAndSetmealById(Integer id);

    void deleteGroupById(Integer id);

}
