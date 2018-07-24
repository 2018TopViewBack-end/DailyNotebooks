package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.service.StoreService;
import com.zhangmiaoxin.www.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AllowOnStoreServlet extends HttpServlet {
    private StoreService ss=new StoreService();
    private UserService us=new UserService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId=0;
        int storeId=0;
        try {
            userId=Integer.parseInt(request.getParameter("userId"));
            storeId=Integer.parseInt(request.getParameter("storeId"));
        } catch (NumberFormatException e) {
            request.setAttribute("message", "该店铺不存在");
            e.printStackTrace();
            request.getRequestDispatcher("OnFood").forward(request,response);
            return;
        }
        if(userId!=0 && storeId!=0 && us.applySellerService(userId) && ss.allowOnStoreService(storeId)){
            request.setAttribute("message","审核通过成功");
            request.getRequestDispatcher("OnStore").forward(request,response);
        }else {
            request.setAttribute("message","审核通过失败");
            request.getRequestDispatcher("OnStore").forward(request,response);
        }
    }
}
