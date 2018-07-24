package com.zhangmiaoxin.www.service;

import com.zhangmiaoxin.www.dao.OrderMapper;
import com.zhangmiaoxin.www.po.*;
import com.zhangmiaoxin.www.util.Judge;
import com.zhangmiaoxin.www.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class OrderService {

    /**
     * 新建一个订单
     * @param order
     * @return 若成功返回该订单的id，若失败则返回0
     */
    public int addOrderService(Order order){
        Date date=new Date();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String orderDate=dateFormat.format(date);
        order.setDate(orderDate);
        order.setStatus("待接单");

        SqlSession session = MybatisUtil.getSession();
        OrderMapper mapper = session.getMapper(OrderMapper.class);
        Integer result = mapper.addOrder(order);
        if(result==1) {
            Integer id = mapper.getOrderId(orderDate);
            session.close();
            return id;
        } else {
            session.close();
            return 0;
        }
    }

    /**
     * 创建订单时创建相对应的订单项服务
     * @param orderItem
     * @return 创建成功返回true
     */
    public boolean addOrderItemService(OrderItem orderItem){
        SqlSession session = MybatisUtil.getSession();
        OrderMapper mapper = session.getMapper(OrderMapper.class);
        int result = mapper.addOrderItem(orderItem);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 查询自己店铺的所有客户订单服务
     * @param storeId
     * @return
     */
    public List<List<Order>> listOrderBySellerService(int storeId) {
        SqlSession session = MybatisUtil.getSession();
        OrderMapper mapper = session.getMapper(OrderMapper.class);

        List<Order> orderList = mapper.listOrderBySeller(storeId);
        if (orderList!=null && orderList.size()!=0) {
            List<Integer> orderIdList = new ArrayList<>();
            List<Integer> receiverIdList = new ArrayList<>();
            for (Order order : orderList) {
                receiverIdList.add(order.getReceiverId());
                orderIdList.add(order.getId());
            }
            List<Receiver> receiverList = new ArrayList<>();
            for (Integer i : receiverIdList){
                receiverList.add(mapper.listOrderReceiver(i));
            }
            List<List<OrderItem>> orderItemListList = listOrderItemService(orderIdList);

            Iterator<Receiver> receiverIterator = receiverList.iterator();
            Iterator<List<OrderItem>> orderItemListIterator = orderItemListList.iterator();
            Iterator<Order> orderIterator = orderList.iterator();
            while (receiverIterator.hasNext() && orderItemListIterator.hasNext() && orderIterator.hasNext()) {
                Order o = orderIterator.next();
                o.setReceiver(receiverIterator.next());
                List<OrderItem> orderItemList=orderItemListIterator.next();
                o.setOrderItemList(orderItemList);
                double price=0.0;
                for(OrderItem oi:orderItemList){
                    price+=oi.getPrice();
                }
                o.setPrice(price);
            }

            List<Order> newOrderList=new ArrayList<>();
            List<Order> oldOrderList=new ArrayList<>();

            for (Order order : orderList) {
                if(order.getStatus().equals("待接单")){
                    newOrderList.add(order);
                }else {
                    oldOrderList.add(order);
                }
            }
            List<List<Order>> allOrderList=new ArrayList<>();
            allOrderList.add(newOrderList);
            allOrderList.add(oldOrderList);
            session.close();

            return allOrderList;
        } else {
            session.close();
            return null;
        }
    }

    /**
     * 商家接单服务
     * @param orderId
     * @return  成功则返回true
     */
    public boolean acceptOrderService(int orderId){
        SqlSession session = MybatisUtil.getSession();
        OrderMapper mapper = session.getMapper(OrderMapper.class);
        Integer result = mapper.acceptOrder(orderId);
        session.close();

        return Judge.judgeUse(result);
    }

    /**
     * 查询某用户的所有订单
     * @param userId
     * @return orderList
     */
    public List<Order> listOrderService(int userId) {
        SqlSession session = MybatisUtil.getSession();
        OrderMapper mapper = session.getMapper(OrderMapper.class);

        List<Integer> receiverIdList = new ArrayList<>();
        List<Integer> storeIdList = new ArrayList<>();
        List<Integer> orderIdList = new ArrayList<>();
        List<Order> orderList = mapper.listOrder(userId);

        if (orderList!=null && orderList.size()!=0) {
            for (Order order : orderList) {
                receiverIdList.add(order.getReceiverId());
                storeIdList.add(order.getStoreId());
                orderIdList.add(order.getId());
            }
            List<Receiver> receiverList = new ArrayList<>();
            List<Store> storeList = new ArrayList<>();
            for (Integer id : receiverIdList) {
                receiverList.add(mapper.listOrderReceiver(id));
            }
            for (Integer id : storeIdList) {
                storeList.add(mapper.listOrderStore(id));
            }
            List<List<OrderItem>> orderItemListList = listOrderItemService(orderIdList);

            Iterator<Receiver> receiverIterator = receiverList.iterator();
            Iterator<Store> storeIterator = storeList.iterator();
            Iterator<List<OrderItem>> orderItemListIterator = orderItemListList.iterator();
            Iterator<Order> orderIterator = orderList.iterator();

            while (receiverIterator.hasNext() && storeIterator.hasNext() &&
                    orderItemListIterator.hasNext() && orderIterator.hasNext()) {
                Order o = orderIterator.next();
                o.setReceiver(receiverIterator.next());
                o.setStore(storeIterator.next());
                List<OrderItem> orderItemList = orderItemListIterator.next();
                o.setOrderItemList(orderItemList);
                double price=0.0;
                for(OrderItem oi:orderItemList){
                    price+=oi.getPrice();
                }
                o.setPrice(price);
            }
        }
        session.close();
        return orderList;
    }

    /**
     * 在查询所有订单时返回对应的OrderItemListList
     * @param orderIdList
     * @return OrderItemListList
     */
    private List<List<OrderItem>> listOrderItemService(List<Integer> orderIdList) {
        SqlSession session = MybatisUtil.getSession();
        OrderMapper mapper = session.getMapper(OrderMapper.class);

        List<List<OrderItem>> orderItemListList = new ArrayList<>();
        for(Integer orderId : orderIdList){
            List<OrderItem> orderItemList1 = mapper.listOrderItem(orderId);

            List<Integer> foodIdList = new ArrayList<>();
            List<Integer> foodNumberList = new ArrayList<>();
            for (OrderItem orderItem : orderItemList1) {
                foodIdList.add(orderItem.getFoodId());
                foodNumberList.add(orderItem.getNumber());
            }
            List<Food> foodList = mapper.listOrderFood(foodIdList);

            List<OrderItem> orderItemList2 = new ArrayList<>();
            Iterator<Food> foodIterator = foodList.iterator();
            Iterator<Integer> numberIterator = foodNumberList.iterator();
            while (foodIterator.hasNext() && numberIterator.hasNext()){
                int number=numberIterator.next();
                Food food=foodIterator.next();
                orderItemList2.add(new OrderItem(number,food,number*food.getPrice()));
            }
            orderItemListList.add(orderItemList2);
        }
        session.close();
        return orderItemListList;
    }

}
