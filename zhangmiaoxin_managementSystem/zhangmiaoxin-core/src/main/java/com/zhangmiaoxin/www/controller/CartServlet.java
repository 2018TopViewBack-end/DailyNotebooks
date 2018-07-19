package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.Receiver;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.ReceiverService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class CartServlet extends HttpServlet {
    private ReceiverService rs=new ReceiverService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Receiver> receiverList=rs.listReceiverService(((User)request.getSession().getAttribute("user")).getId());
        request.setAttribute("receiverList",receiverList);

        int storeId=0;
        try {
            storeId=Integer.parseInt(request.getParameter("storeId"));
        } catch (NumberFormatException e) {
            request.setAttribute("error","出错了，请稍后重试");
            request.getRequestDispatcher("cart.jsp").forward(request,response);
            e.printStackTrace();
            return;
        }
        List<Order> cartOrderList=(List<Order>) request.getSession().getAttribute("cart");
        for (Order order:cartOrderList) {
            if(order.getStore().getId() == storeId){
                request.setAttribute("order",order);
                request.getRequestDispatcher("cartPay.jsp").forward(request,response);
            }
        }
    }
}
