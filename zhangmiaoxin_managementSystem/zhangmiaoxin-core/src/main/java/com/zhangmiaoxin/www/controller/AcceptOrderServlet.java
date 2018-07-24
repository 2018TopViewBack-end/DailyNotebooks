package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AcceptOrderServlet extends HttpServlet {
    private OrderService os=new OrderService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int orderId;
        try {
            orderId=Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            request.setAttribute("orderMessage","该订单不存在");
            request.getRequestDispatcher("SellerOrder").forward(request,response);
            e.printStackTrace();
            return;
        }

        if(os.acceptOrderService(orderId)){
            request.setAttribute("orderMessage","接单成功");
            request.getRequestDispatcher("SellerOrder").forward(request,response);
        }else {
            request.setAttribute("orderMessage","接单失败");
            request.getRequestDispatcher("SellerOrder").forward(request,response);
        }

    }
}
