package com.zhangmiaoxin.www.service;

import com.zhangmiaoxin.www.dao.FoodMapper;
import com.zhangmiaoxin.www.po.Food;
import com.zhangmiaoxin.www.util.Judge;
import com.zhangmiaoxin.www.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class FoodService {

    /**
     * 查看店内所有商品服务（顾客）
     * @param storeId
     * @return
     */
    public List<Food> listFoodService(int storeId){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        List<Food> foodList = mapper.listFood(storeId);
        session.close();

        return foodList;
    }

    /**
     * 查看店内所有商品（商家自己）
     * @param storeId
     * @return
     */
    public List<Food> selectFoodServiceBySelf(int storeId){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        List<Food> foodList = mapper.listFoodByStore(storeId);
        session.close();

        return foodList;
    }

    /**
     * 商家增加菜品，成功则返回true
     * @param food
     * @return
     */
    public boolean addFoodService(Food food){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        Integer result = mapper.addFood(food);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 更新已经上架的菜品（有库存）
     * @param food
     * @return
     */
    public boolean updateFoodService(Food food){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        Integer result = mapper.updateFood(food);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 更新没上架的菜品（没库存）
     * @param food
     * @return
     */
    public boolean updateFoodService2(Food food){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        Integer result = mapper.updateFood2(food);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 管理员查询所有店铺的待审核商品服务，将不同店铺的待审核商品放在不同的list中
     * @return  一个list，里面包含所有店铺的待审核商品list
     */
    public List<List<Food>> onFoodService(){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        List<Food> allOnFoodList = mapper.listOnFood();
        session.close();

        List<List<Food>> storeOnFoodList=new ArrayList<>();
        if(allOnFoodList!=null && allOnFoodList.size()>0) {
            List<Food> oneStoreFoodList = new ArrayList<>();
            for (int i = 0; i < allOnFoodList.size(); i++) {
                if (i == 0) {
                    oneStoreFoodList.add(allOnFoodList.get(i));
                } else {
                    if (allOnFoodList.get(i - 1).getStoreId() == allOnFoodList.get(i).getStoreId()) {
                        oneStoreFoodList.add(allOnFoodList.get(i));
                    } else {
                        storeOnFoodList.add(oneStoreFoodList);
                        oneStoreFoodList = new ArrayList<>();
                        oneStoreFoodList.add(allOnFoodList.get(i));
                    }
                }
            }
            if(oneStoreFoodList.size()>0) {
                storeOnFoodList.add(oneStoreFoodList);
            }
        }
        return storeOnFoodList;
    }

    /**
     * 管理员审核通过待上架菜品服务
     * @param foodId
     * @return  审核通过成功返回true
     */
    public boolean allowOnFoodService(int foodId){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        Integer result = mapper.allowOnFood(foodId);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 管理员审核不通过待上架菜品服务
     * @param foodId
     * @return  审核不通过成功返回true
     */
    public boolean refuseOnFoodService(int foodId){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        Integer result = mapper.refuseOnFood(foodId);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 更新菜品销量服务
     * @param num
     * @param foodId
     * @return
     */
    public boolean updateFoodSalesService(int num, int foodId){
        SqlSession session = MybatisUtil.getSession();
        FoodMapper mapper = session.getMapper(FoodMapper.class);
        Integer result = mapper.updateFoodSales(num,foodId);
        session.close();

        return Judge.judgeUse(result);
    }


}
