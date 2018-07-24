package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Food;
import com.zhangmiaoxin.www.service.FoodService;
import com.zhangmiaoxin.www.util.Constants;
import com.zhangmiaoxin.www.util.Pic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AddFoodServlet extends HttpServlet {
    private FoodService fs=new FoodService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> paramMap=new HashMap<>();

        int storeId=0;
        String dishName="";
        double dishPrice=0.0;
        String dishDes="";
        String pic="";

        paramMap.put("storeId","");
        paramMap.put("dishName","");
        paramMap.put("dishPrice","");
        paramMap.put("dishDes","");
        paramMap.put("pic","");

        paramMap= Pic.uploadPic(request,paramMap);

        for (Map.Entry<String,String> entry:paramMap.entrySet()){
            switch (entry.getKey()){
                case "storeId":
                    try {
                        storeId=Integer.parseInt(entry.getValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        request.setAttribute("addError","您的店铺id出现问题，请重试");
                        request.getRequestDispatcher("MyStore").forward(request,response);
                        return;
                    }
                    break;
                case "dishName":
                    dishName=entry.getValue();
                    break;
                case "dishPrice":
                    try {
                        dishPrice=Double.parseDouble(entry.getValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        request.setAttribute("addError","菜品价格只能为大于0的数字，且小数点后最多两位");
                        request.getRequestDispatcher("MyStore").forward(request,response);
                        return;
                    }
                    break;
                case "dishDes":
                    dishDes=entry.getValue();
                    break;
                case "pic":
                    pic=entry.getValue();
            }
        }
        if(dishPrice<0){
            request.setAttribute("addError","菜品价格只能为大于0的数字，且小数点后最多两位");
            request.getRequestDispatcher("MyStore").forward(request,response);
            return;
        }
        if(pic.equals("")){
            pic=Constants.DEFAULT_PIC;    //如果没有上传图片，把图片的地址设为默认地址
        }

        Food food=new Food(dishName,dishPrice,pic,dishDes,storeId);
        if(fs.addFoodService(food)){
            request.setAttribute("message","新增菜品成功，请等待管理员审核");
            request.getRequestDispatcher("MyStore").forward(request,response);
        }else {
            request.setAttribute("message","很抱歉，新增菜品失败，请重试");
            request.getRequestDispatcher("MyStore").forward(request,response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
