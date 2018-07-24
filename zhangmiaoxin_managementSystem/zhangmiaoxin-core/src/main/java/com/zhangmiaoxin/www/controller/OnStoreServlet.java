package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Store;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.StoreService;
import com.zhangmiaoxin.www.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class OnStoreServlet extends HttpServlet {
    private StoreService ss=new StoreService();
    private UserService us=new UserService();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       doPost(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Store> storeList=ss.onStoreService();

        if(storeList.size()<1||storeList==null){
            request.setAttribute("noStore","暂时还没有待审核的店铺哦");
            request.getRequestDispatcher("onStore.jsp").forward(request,response);
        }else {
            List<User> userList=new ArrayList<>();
            Iterator<Store> storeIterator=storeList.iterator();
            while (storeIterator.hasNext()){
                Store store=storeIterator.next();
                User user=us.onStoreUserService(store.getOwnerId());
                if(user==null){
                    storeIterator.remove();
                }else {
                userList.add(user);
                }
            }
            if(userList.size()!=storeList.size()){
                request.setAttribute("error","出现了一些错误，请稍后再试");
                request.getRequestDispatcher("onStore.jsp").forward(request,response);
                return;
            }
            if(storeList.size()<1||storeList==null){
                request.setAttribute("noStore","暂时还没有待审核的店铺哦");
                request.getRequestDispatcher("onStore.jsp").forward(request,response);
            }
            request.setAttribute("storeList",storeList);
            request.setAttribute("userList",userList);
            request.getRequestDispatcher("onStore.jsp").forward(request,response);
        }
    }
}
