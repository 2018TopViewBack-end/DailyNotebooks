package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.service.FoodService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AllowOnFoodServlet extends HttpServlet {
    private FoodService fs=new FoodService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int foodId=0;
        try {
            foodId=Integer.parseInt(request.getParameter("foodId"));
        } catch (NumberFormatException e) {
            request.setAttribute("message", "该商品不存在");
            e.printStackTrace();
            request.getRequestDispatcher("OnFood").forward(request,response);
            return;
        }
        if(foodId!=0 && fs.allowOnFoodService(foodId)){
            request.setAttribute("message","审核通过成功");
            request.getRequestDispatcher("OnFood").forward(request,response);
        }else {
            request.setAttribute("message","审核通过失败");
            request.getRequestDispatcher("OnFood").forward(request,response);
        }
    }
}
