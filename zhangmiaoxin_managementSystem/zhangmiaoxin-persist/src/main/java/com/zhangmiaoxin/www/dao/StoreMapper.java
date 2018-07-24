package com.zhangmiaoxin.www.dao;

import com.zhangmiaoxin.www.po.Store;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StoreMapper {
    /**
     * 用户登录后主界面，显示所有可用的店铺
     * @return 一个店铺List
     */
    List<Store> listStore();

    /**
     * 更新商家的销量
     * @param sales
     * @param storeId
     * @return int result
     */
    int updateSales(@Param("sales") int sales, @Param("storeId") int storeId);

    /**
     * 店主查看自己的店铺
     * @param userId
     * @return Store
     */
    Store getStore(int userId);

    /**
     * 店主查看自己的店铺是否已经通过审核
     * @param userId
     * @return 店铺是否已经通过审核
     */
    String checkUsable(int userId);

    /**
     * 查询待上架的商品时（管理员）需要的店铺信息
     * @param storeIdList
     * @return 一个店铺List
     */
    List<Store> listStoreByAdmin(List<Integer> storeIdList);

    /**
     * 禁止用户重复提交开店申请
     * @param userId
     * @return storeId
     */
    int getStoreId(int userId);


    /**
     * 管理员查询所有待审批的店铺
     * @return 待审批的StoreList
     */
    List<Store> listOnStore();

    /**
     * 管理员通过店铺申请
     * @param storeId
     * @return int result
     */
    int allowStore(int storeId);

    /**
     * 通过店铺id查询店铺名称
     * @param storeId
     * @return storeName
     */
    String selectStoreName(int storeId);

    /**
     * 普通用户申请成为商家
     * @param store
     * @return 申请是否提交成功，成功返回1
     */
    int applyStore(Store store);
}
