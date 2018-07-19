package com.zhangmiaoxin.www.dao;

import com.zhangmiaoxin.www.po.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    /**
     * 新建一个订单，若成功返回该订单的id，若失败则返回0
     * @param order
     * @return
     */
    Integer addOrder(Order order);

    /**
     * 获取刚刚创建的订单编号
     * @param date
     * @return
     */
    Integer getOrderId(String date);

    /**
     * 新建一个订单项
     * @param orderItem
     * @return
     */
    Integer addOrderItem(OrderItem orderItem);

    /**
     * 查询某用户的所有订单
     * @param userId
     * @return
     */
    List<Order> listOrder(int userId);

    /**
     * 在查询订单时返回对应的ReceiverList
     * @param id
     * @return
     */
    Receiver listOrderReceiver(Integer id);

    /**
     * 在查询订单时返回对应的StoreList
     * @param id
     * @return
     */
    Store listOrderStore(Integer id);

    /**
     * 在查询所有订单时返回对应的OrderItemListList
     * @param orderId
     * @return
     */
    List<OrderItem> listOrderItem(Integer orderId);

    /**
     * 在查询订单时返回对应的foodList
     * @param foodIdList
     * @return
     */
    List<Food> listOrderFood(List<Integer> foodIdList);

    /**
     * 商家接单
     * @param id
     * @return
     */
    Integer acceptOrder(int id);

    /**
     * 商家查询自己店铺的订单
     * @param storeId
     * @return
     */
    List<Order> listOrderBySeller(int storeId);
}
