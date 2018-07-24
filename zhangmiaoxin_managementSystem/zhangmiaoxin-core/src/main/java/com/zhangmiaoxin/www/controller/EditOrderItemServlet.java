package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.OrderItem;
import com.zhangmiaoxin.www.po.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class EditOrderItemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> cartOrderList=(List<Order>) request.getSession().getAttribute("cart");
        int storeId=Integer.parseInt(request.getParameter("storeId"));
        int foodId=Integer.parseInt(request.getParameter("foodId"));
        int num=Integer.parseInt(request.getParameter("num"));

        for (int j=0; j<cartOrderList.size();j++) {
            Order order=cartOrderList.get(j);
            if(order.getStore().getId()==storeId){
                List<OrderItem> orderItemList=order.getOrderItemList();
                for (int i = 0; i < orderItemList.size(); i++) {
                    OrderItem orderItem=orderItemList.get(i);
                    if(orderItem.getFood().getId()==foodId){
                        orderItem.setNumber(num);
                        orderItemList.set(i,orderItem);
                        order.setOrderItemList(orderItemList);
                        cartOrderList.set(j,order);
                        request.getSession().setAttribute("cart",cartOrderList);
                        request.getRequestDispatcher("cart.jsp").forward(request,response);
                    }
                }
            }
        }
    }
}
