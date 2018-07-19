package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Food;
import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.OrderItem;
import com.zhangmiaoxin.www.po.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddCartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int foodId= 0;
        int storeId=0;
        try {
            storeId=Integer.parseInt(request.getParameter("storeId"));
            foodId = Integer.parseInt(request.getParameter("foodId"));
        } catch (NumberFormatException e) {
            request.setAttribute("error","出错了，去别的店看看吧");
            e.printStackTrace();
            request.getRequestDispatcher("scanStore").forward(request,response);
            return;
        }
        String storeName=request.getParameter("storeName");
        String foodName=request.getParameter("foodName");
        String pic=request.getParameter("pic");
        double foodPrice=Double.parseDouble(request.getParameter("foodPrice"));

        HttpSession session=request.getSession();
        List<Order> cartOrderList=(List<Order>) session.getAttribute("cart");
        boolean markItem=false;
        boolean markOrder=false;
        Food food=new Food(foodId,foodName,foodPrice,pic);

        for (Order cartOrder:cartOrderList ) {
            if(cartOrder.getStore().getId()==storeId){
                List<OrderItem> orderItemList=cartOrder.getOrderItemList();
                for (OrderItem orderItem:orderItemList) {
                    if(orderItem.getFood().getId()==foodId){
                        orderItem.setNumber(orderItem.getNumber()+1);
                        orderItem.setPrice((orderItem.getFood().getPrice())*orderItem.getNumber());
                        markItem=true;
                        break;
                    }
                }
                if(!markItem){
                    orderItemList.add(new OrderItem(1,food,foodPrice));
                }
                markOrder=true;
            }
        }
        if(!markOrder){
            List<OrderItem> orderItemList=new ArrayList<OrderItem>();
            orderItemList.add(new OrderItem(1,food,foodPrice));
            Order order=new Order(new Store(storeId,storeName),orderItemList);
            cartOrderList.add(order);
        }
        session.setAttribute("cart",cartOrderList);
        request.setAttribute("success","添加商品到购物车成功");
        request.getRequestDispatcher("Food?storeId="+storeId).forward(request,response);
    }
}
