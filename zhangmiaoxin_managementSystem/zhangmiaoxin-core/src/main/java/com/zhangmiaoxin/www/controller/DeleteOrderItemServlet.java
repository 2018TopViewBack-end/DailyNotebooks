package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.OrderItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class DeleteOrderItemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> cartOrderList=(List<Order>) request.getSession().getAttribute("cart");
        int storeId=Integer.parseInt(request.getParameter("storeId"));
        int foodId=Integer.parseInt(request.getParameter("foodId"));

        for (Order order:cartOrderList) {
            if(order.getStore().getId()==storeId){
                List<OrderItem> orderItemList=order.getOrderItemList();
                Iterator<OrderItem> itemIterator=orderItemList.iterator();
                while(itemIterator.hasNext()){
                    if(itemIterator.next().getFood().getId()==foodId){
                        itemIterator.remove();
                        request.getSession().setAttribute("cart",cartOrderList);
                        request.getRequestDispatcher("cart.jsp").forward(request,response);
                    }
            }
        }
    }
    }
}
