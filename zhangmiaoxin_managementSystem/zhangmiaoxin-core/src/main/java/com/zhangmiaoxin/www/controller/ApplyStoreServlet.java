package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Store;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.StoreService;
import com.zhangmiaoxin.www.util.Constants;
import com.zhangmiaoxin.www.util.Pic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ApplyStoreServlet extends HttpServlet {
    private StoreService ss=new StoreService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String,String> paramMap=new HashMap<>();

        String pic="";
        String storeName="";
        String storeTel="";
        String address="";
        int categoryId=0;

        paramMap.put("pic","");
        paramMap.put("storeName","");
        paramMap.put("storeTel","");
        paramMap.put("address","");
        paramMap.put("categoryId","");

        paramMap= Pic.uploadPic(request,paramMap);
        for (Map.Entry<String,String> entry:paramMap.entrySet()){
            switch (entry.getKey()){
                case "pic":
                    pic = entry.getValue();
                    break;
                case "storeName":
                    storeName=entry.getValue();
                    break;
                case "storeTel":
                    storeTel=entry.getValue();
                    break;
                case "address":
                    address=entry.getValue();
                    break;
                case "categoryId":
                    try {
                        categoryId=Integer.parseInt(entry.getValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        request.setAttribute("message","您的店铺分类出现问题，请重试");
                        request.getRequestDispatcher("GetCategory").forward(request,response);
                        return;
                    }
            }
        }
        if(!(categoryId>0)){
            request.setAttribute("message","您的店铺分类出现问题，请重试");
            request.getRequestDispatcher("GetCategory").forward(request,response);
            return;
        }
        if(pic.equals("")){
            pic=Constants.DEFAULT_PIC;    //如果没有上传图片，把图片的地址设为默认地址
        }

        int ownerId=((User)request.getSession().getAttribute("user")).getId();
        Store store=new Store(storeName,storeTel,address,pic,ownerId,categoryId);
        if(ss.applyStoreService(store)){
            request.setAttribute("message","您的开店申请已经提交成功，请等待管理员审核");
            request.getRequestDispatcher("scanStore").forward(request,response);
        }else {
            request.setAttribute("message","很抱歉，您的开店申请提交失败。若之前已经提交过申请则不可以再重复提交，若无，请重试");
            request.getRequestDispatcher("GetCategory").forward(request,response);
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
