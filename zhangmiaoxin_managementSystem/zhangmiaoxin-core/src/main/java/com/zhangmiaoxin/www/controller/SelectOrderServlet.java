package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class SelectOrderServlet extends HttpServlet {
    private OrderService os = new OrderService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId=((User)request.getSession().getAttribute("user")).getId();
        List<Order> orderList=os.listOrderService(userId);

        if(orderList==null || orderList.size()==0) {
            request.setAttribute("noOrder","您还没有在本网站下过单呢，去看看有什么好吃的？");
            request.getRequestDispatcher("selectOrder.jsp").forward(request,response);
        } else {
            request.setAttribute("orderList",orderList);
            request.getRequestDispatcher("selectOrder.jsp").forward(request,response);
        }
    }
}
