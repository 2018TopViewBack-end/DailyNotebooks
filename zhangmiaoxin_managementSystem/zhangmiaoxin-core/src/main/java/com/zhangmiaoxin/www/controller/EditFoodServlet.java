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


public class EditFoodServlet extends HttpServlet {
    private FoodService fs=new FoodService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> paramMap=new HashMap<String, String>();

        String oldPic=Constants.DEFAULT_PIC;    //先把旧图片的地址设为默认地址

        int id=0;
        String newFoodName="";
        double newFoodPrice=0.0;
        String newFoodPic="";
        String newFoodDes="";
        int newStock=-1;
        String usable="";

        paramMap.put("foodId","");
        paramMap.put("newName","");
        paramMap.put("usable","");
        paramMap.put("oldPic",oldPic);
        paramMap.put("pic","");
        paramMap.put("newPrice","");
        paramMap.put("newStock","");
        paramMap.put("newDes","");

        paramMap= Pic.uploadPic(request,paramMap);

        for (Map.Entry<String,String> entry: paramMap.entrySet()) {
            switch (entry.getKey()){
                case "foodId":
                    try {
                        id=Integer.parseInt(entry.getValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    break;
                case "newName":
                    newFoodName=entry.getValue();
                    break;
                case "newDes":
                    newFoodDes=entry.getValue();
                    break;
                case "pic":
                    newFoodPic=entry.getValue();
                    break;
                case "oldPic":
                    oldPic=entry.getValue();
                    break;
                case "usable":
                    usable=entry.getValue();
                    break;
                case "newStock":
                    if(usable.equals("true")){
                        try {
                            newStock=Integer.parseInt(entry.getValue());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            request.setAttribute("editError","菜品库存只能为正整数或0，请重新填写");
                            request.getRequestDispatcher("MyStore").forward(request,response);
                            return;
                        }
                    }
                    break;
                case "newPrice":
                    try {
                        newFoodPrice=Double.parseDouble(entry.getValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        request.setAttribute("editError","商品价格只能为数字，小数点后最多保留两位，请重新填写");
                        request.getRequestDispatcher("MyStore").forward(request,response);
                        return;
                    }
            }
        }
        if(newFoodPrice<0){
            request.setAttribute("editError","商品价格只能为正数，小数点后最多保留两位，请重新填写");
            request.getRequestDispatcher("MyStore").forward(request,response);
            return;
        }
        if(newStock<0&&usable.equals("true")){
            request.setAttribute("editError","菜品库存只能为正整数或0，请重新填写");
            request.getRequestDispatcher("MyStore").forward(request,response);
            return;
        }
        if(newFoodPic.equals("")){
            newFoodPic=oldPic;
        }

        if(usable.equals("true")){
            Food food=new Food(id,newFoodName,newFoodPrice,newStock,newFoodPic,newFoodDes);
            if(fs.updateFoodService(food)){
                request.setAttribute("message","更新菜品信息成功");
                request.getRequestDispatcher("MyStore").forward(request,response);
            }else {
                request.setAttribute("message","更新菜品信息失败");
                request.getRequestDispatcher("MyStore").forward(request,response);
            }
        } else {
            Food food=new Food(id,newFoodName,newFoodPrice,newFoodPic,newFoodDes);
            if(fs.updateFoodService2(food)){
                request.setAttribute("message","更新菜品信息成功");
                request.getRequestDispatcher("MyStore").forward(request,response);
            }else {
                request.setAttribute("message   ","更新菜品信息失败");
                request.getRequestDispatcher("MyStore").forward(request,response);
            }
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
