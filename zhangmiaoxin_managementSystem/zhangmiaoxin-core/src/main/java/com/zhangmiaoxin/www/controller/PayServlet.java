package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Food;
import com.zhangmiaoxin.www.po.Receiver;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.ReceiverService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PayServlet extends HttpServlet {
    private ReceiverService rs = new ReceiverService();
    static int orderNum=0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Receiver> receiverList=rs.listReceiverService(((User)request.getSession().getAttribute("user")).getId());
        request.setAttribute("receiverList",receiverList);

        String storeName=request.getParameter("storeName");
        request.setAttribute("storeName",storeName);

        List<Food> foodList=new ArrayList<>();
        //直接下单的情况
        if(request.getParameter("payWay")!=null&&request.getParameter("payWay").equals("direct")){
            int foodId=Integer.parseInt(request.getParameter("foodId"));
            String foodName=request.getParameter("foodName");
            double foodPrice=Double.parseDouble(request.getParameter("foodPrice"));
            int storeId=Integer.parseInt(request.getParameter("storeId"));

            Food singleFood=new Food(foodId,foodName,foodPrice,storeId);
            foodList.add(singleFood);
        }
        request.setAttribute("foodList",foodList);
        orderNum++;
        request.getRequestDispatcher("directPay.jsp").forward(request,response);
    }
}
