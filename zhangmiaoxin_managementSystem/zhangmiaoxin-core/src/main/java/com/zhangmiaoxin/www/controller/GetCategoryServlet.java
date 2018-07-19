package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Category;
import com.zhangmiaoxin.www.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class GetCategoryServlet extends HttpServlet {
    private CategoryService cs = new CategoryService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categoryList=cs.listCategoryService();
        request.setAttribute("categoryList",categoryList);
        request.getRequestDispatcher("applyStore.jsp").forward(request,response);
    }
}
