package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.Store;
import com.zhangmiaoxin.www.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class SellerOrderServlet extends HttpServlet {
    private OrderService os=new OrderService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int storeId = ((Store) request.getSession().getAttribute("myStore")).getId();

        List<List<Order>> storeOrderList = os.listOrderBySellerService(storeId);
        if (storeOrderList == null || (storeOrderList.get(0).size() == 0 && storeOrderList.get(1).size()==0)||
                (storeOrderList.get(0)==null &&storeOrderList.get(1)==null)) {
            request.setAttribute("storeNoOrder", "啊哦，您的店铺中暂时还没有订单");
            request.getRequestDispatcher("sellerOrder.jsp").forward(request, response);
        } else {
            request.setAttribute("storeOrderList", storeOrderList);
            request.getRequestDispatcher("sellerOrder.jsp").forward(request, response);
        }
    }
}
