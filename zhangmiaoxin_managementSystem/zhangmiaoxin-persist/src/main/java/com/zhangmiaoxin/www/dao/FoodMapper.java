package com.zhangmiaoxin.www.dao;

import com.zhangmiaoxin.www.po.Food;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FoodMapper {

    /**
     * 查询某店铺中的所有食物
     * @param storeId
     * @return 返回一个食物List
     */
    List<Food> listFood(int storeId);

    /**
     * 查询某店铺中的所有食物（商家自己用）
     * @param storeId
     * @return 返回一个食物List
     */
    List<Food> listFoodByStore(int storeId);

    /**
     * 更新售出的食物的库存和销量
     * @param num
     * @param foodId
     * @return
     */
    Integer updateFoodSales(@Param("num") int num, @Param("foodId") int foodId);

    /**
     * 商家增加菜品
     * @param food
     * @return
     */
    Integer addFood(Food food);

    /**
     * 商家更新菜品信息
     * @param food
     * @return
     */
    Integer updateFood(Food food);

    /**
     * 商家更新菜品信息(未上架的)
     * @param food
     * @return
     */
    Integer updateFood2(Food food);

    /**
     * 网站管理员查询所有待审批的商品
     * @return
     */
    List<Food> listOnFood();

    /**
     * 管理员审核通过待上架菜品
     * @param foodId
     * @return
     */
    Integer allowOnFood(int foodId);

    /**
     * 管理员审核不通过待上架菜品
     * @param foodId
     * @return
     */
    Integer refuseOnFood(int foodId);
}
