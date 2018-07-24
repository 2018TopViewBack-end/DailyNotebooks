package com.zhangmiaoxin.www.service;

import com.zhangmiaoxin.www.dao.CategoryMapper;
import com.zhangmiaoxin.www.po.Category;
import com.zhangmiaoxin.www.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class CategoryService {

    /**
     * 查询所有店铺分类
     * @return
     */
    public List<Category> listCategoryService() {
        SqlSession session = MybatisUtil.getSession();
        CategoryMapper mapper = session.getMapper(CategoryMapper.class);
        List<Category> categoryList = mapper.listCategory();
        session.close();

        return categoryList;
    }

    /**
     * 管理员审批店铺时要用到的查询店铺分类名称
     * @param id
     * @return
     */
    public String getCategoryServicce(int id) {
        SqlSession session = MybatisUtil.getSession();
        CategoryMapper mapper = session.getMapper(CategoryMapper.class);
        String category = mapper.getCategory(id);
        session.close();

        return category;
    }
}
