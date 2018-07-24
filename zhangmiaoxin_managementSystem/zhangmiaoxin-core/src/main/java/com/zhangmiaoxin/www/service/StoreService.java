package com.zhangmiaoxin.www.service;


import com.zhangmiaoxin.www.dao.StoreMapper;
import com.zhangmiaoxin.www.po.Store;
import com.zhangmiaoxin.www.util.Judge;
import com.zhangmiaoxin.www.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class StoreService {

    /**
     * 用户登录后主界面，显示所有可用的店铺
     * @return 一个店铺List
     */
    public List<Store> listStoreService() {
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        List<Store> storeList = storeMapper.listStore();
        session.close();

        return storeList;
    }

    /**
     * 更新商家销量服务
     * @param num
     * @param storeId
     * @return
     */
    public boolean updateSalesService(int num, int storeId){
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        int result = storeMapper.updateSales(num,storeId);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 店主查看自己的店铺
     * @param userId
     * @return
     */
    public Store selectMyStore(int userId){
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        boolean usable = true;
        if(storeMapper.checkUsable(userId).equals("2")){
            usable = false;
        }
        Store store = storeMapper.getStore(userId);
        store.setUsable(usable);
        session.close();

        return store;
    }

    /**
     * 查询待上架的商品时（管理员）需要的店铺信息
     * @param storeIdList
     * @return
     */
    public List<Store> selectStoreByIdService(List<Integer> storeIdList){
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        List<Store> storeList = storeMapper.listStoreByAdmin(storeIdList);
        session.close();

        return storeList;
    }

    /**
     * 普通用户申请开店服务
     * @param store
     * @return
     */
    public boolean applyStoreService(Store store){
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        Integer isDouble=storeMapper.getStoreId(store.getOwnerId());

        if(isDouble <= 0){
            session.close();
            return false;
        }else {
            int result = storeMapper.applyStore(store);
            session.close();
            return Judge.judgeUse(result);
        }
    }

    /**
     * 管理员查询所有待审批的店铺
     * @return
     */
    public List<Store> onStoreService(){
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        List<Store> storeList = storeMapper.listOnStore();
        session.close();

        return storeList;
    }

    /**
     * 管理员通过店铺申请
     * @param storeId
     * @return
     */
    public boolean allowOnStoreService(int storeId){
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        int result = storeMapper.allowStore(storeId);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 通过店铺id查询店铺名称服务
     * @param storeId
     * @return
     */
    public String selectStoreNameService(int storeId){
        SqlSession session = MybatisUtil.getSession();
        StoreMapper storeMapper = session.getMapper(StoreMapper.class);
        String storeName = storeMapper.selectStoreName(storeId);
        session.close();

        return storeName;
    }

}
