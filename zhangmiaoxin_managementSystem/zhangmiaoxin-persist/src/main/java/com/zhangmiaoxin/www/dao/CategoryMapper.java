package com.zhangmiaoxin.www.dao;

import com.zhangmiaoxin.www.po.Category;

import java.util.List;

public interface CategoryMapper {

    /**
     * 查询所有店铺分类
     * @return
     */
    List<Category> listCategory();

    /**
     * 管理员审批店铺时要用到的查询店铺分类名称
     * @param id
     * @return
     */
    String getCategory(int id);
}
