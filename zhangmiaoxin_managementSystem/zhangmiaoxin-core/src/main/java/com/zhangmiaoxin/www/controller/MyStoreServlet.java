package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Food;
import com.zhangmiaoxin.www.po.Store;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.FoodService;
import com.zhangmiaoxin.www.service.StoreService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class MyStoreServlet extends HttpServlet {
    private StoreService ss = new StoreService();
    private FoodService fs=new FoodService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId=((User)request.getSession().getAttribute("user")).getId();
        Store store=ss.selectMyStore(userId);
        //判断该商家账号是否已开店
        if(store!=null){
            List<Food> foodList=fs.selectFoodServiceBySelf(store.getId());
            if(!foodList.isEmpty()){    //判断该店铺中是否有已经上架的食物(不管是否可销售)
                request.setAttribute("myStore",store);
                request.setAttribute("foodList",foodList);
                request.getRequestDispatcher("seller.jsp").forward(request,response);
            } else {
                request.setAttribute("myStore",store);
                request.setAttribute("noFood","您的店铺中还没有上架的菜品噢，现在去上一个？");
                request.getRequestDispatcher("seller.jsp").forward(request,response);
            }
        }
    }
}
