package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Order;
import com.zhangmiaoxin.www.po.OrderItem;
import com.zhangmiaoxin.www.po.User;
import com.zhangmiaoxin.www.service.FoodService;
import com.zhangmiaoxin.www.service.OrderService;
import com.zhangmiaoxin.www.service.StoreService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SetOrderServlet extends HttpServlet {

    private OrderService os = new OrderService();
    private FoodService fs = new FoodService();
    private StoreService ss = new StoreService();
    //用于判断是否刷新进入该servlet，避免用户刷新创建新的相同订单
    private int orderNum=0;
    private String orderDate=null;
    private int orderId=0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //用于判断是否刷新进入该servlet，避免用户刷新创建新的相同订单
        if(orderNum==PayServlet.orderNum){
            request.setAttribute("orderId",orderId);
            request.setAttribute("orderDate",orderDate);
            request.getRequestDispatcher("successOrder.jsp").forward(request,response);
            return;
        }

        int receiverId=Integer.parseInt(request.getParameter("receiverId"));
        String message=request.getParameter("message");
        double price=Double.parseDouble(request.getParameter("price"));
        int storeId=Integer.parseInt(request.getParameter("storeId"));
        int userId=((User)request.getSession().getAttribute("user")).getId();
        int foodId=Integer.parseInt(request.getParameter("foodId"));
        String foodName=request.getParameter("foodName");
        double foodPrice=Double.parseDouble(request.getParameter("foodPrice"));
        int number=Integer.parseInt(request.getParameter("number"));

        Order order=new Order(userId,storeId,receiverId,message);
        this.orderId=os.addOrderService(order);
        //如果成功创建订单
        if(orderId>0){
            OrderItem orderItem=new OrderItem(foodId,orderId,number,
                    ((User)request.getSession().getAttribute("user")).getId(),price);
            boolean result = os.addOrderItemService(orderItem);
            //为订单创建相应订单项，成功跳转到下单成功页面，失败则跳转到下单失败页面
            if(result) {
                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                orderDate = dateFormat.format(date);

                request.setAttribute("orderId",orderId);
                request.setAttribute("orderDate",orderDate);
                orderNum++;
                //更新商品的销量和库存，更新商家的销量
                if(fs.updateFoodSalesService(number,foodId) && ss.updateSalesService(number,storeId)) {
                    request.getRequestDispatcher("successOrder.jsp").forward(request, response);
                }else {
                    request.getRequestDispatcher("failureOrder.jsp").forward(request,response);
                }
            }else {
                request.getRequestDispatcher("failureOrder.jsp").forward(request,response);
            }
        }else {
            request.getRequestDispatcher("failureOrder.jsp").forward(request,response);
        }
    }
}
