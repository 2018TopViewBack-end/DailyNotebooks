package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Store;
import com.zhangmiaoxin.www.service.StoreService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class StoreServlet extends HttpServlet {
    private StoreService ss = new StoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Store> allStoreList=ss.listStoreService();
        request.setAttribute("allStoreList",allStoreList);
        request.getRequestDispatcher("scanStore.jsp").forward(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
