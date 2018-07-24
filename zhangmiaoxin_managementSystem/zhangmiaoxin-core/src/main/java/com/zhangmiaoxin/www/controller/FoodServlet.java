package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Food;
import com.zhangmiaoxin.www.service.FoodService;
import com.zhangmiaoxin.www.service.StoreService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class FoodServlet extends HttpServlet {
    private FoodService fs=new FoodService();
    private StoreService ss=new StoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取从表格中传来的店铺ID
        Integer storeId= null;
        try {
            storeId = Integer.parseInt(request.getParameter("storeId"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if(storeId!=null){
            String storeName=ss.selectStoreNameService(storeId);
            List<Food> foodList=fs.listFoodService(storeId);
            if(!foodList.isEmpty()){    //判断该店铺中是否有已经上架的食物
                request.setAttribute("storeName",storeName);
                request.setAttribute("foodList",foodList);
                request.getRequestDispatcher("food.jsp").forward(request,response);
            }
        }else {
            request.setAttribute("null","很抱歉，该店铺不存在，返回去看看别的店铺吧");
            request.getRequestDispatcher("food.jsp").forward(request,response);
        }
    }
}
