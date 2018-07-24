package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Food;
import com.zhangmiaoxin.www.po.Store;
import com.zhangmiaoxin.www.service.FoodService;
import com.zhangmiaoxin.www.service.StoreService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class OnFoodServlet extends HttpServlet {
    private FoodService fs=new FoodService();
    private StoreService ss=new StoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<List<Food>> storeOnFoodList=fs.onFoodService();

        if(storeOnFoodList==null||storeOnFoodList.size()==0){
            request.setAttribute("noFood","暂时还没有待审核的产品哦");
            request.getRequestDispatcher("onFood.jsp").forward(request,response);
        } else {
            List<Integer> storeIdList=new ArrayList<>();
            for (List<Food> oneStoreOnFoodList:storeOnFoodList) {
                storeIdList.add(oneStoreOnFoodList.get(0).getStoreId());
            }
            List<Store> storeList=ss.selectStoreByIdService(storeIdList);
            request.setAttribute("storeList",storeList);
            request.setAttribute("storeOnFoodList",storeOnFoodList);
            request.getRequestDispatcher("onFood.jsp").forward(request,response);
        }
    }
}
