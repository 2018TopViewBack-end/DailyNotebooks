package com.zhangmiaoxin.www.controller;

import com.zhangmiaoxin.www.po.Food;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class CartPayServlet extends HttpServlet {
    private OrderService os=new OrderService();
    private FoodService fs=new FoodService();
    private StoreService ss=new StoreService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int storeId=0;
        int receiverId=0;
        try {
            receiverId=Integer.parseInt(request.getParameter("receiverId"));
            storeId=Integer.parseInt(request.getParameter("storeId"));
        } catch (NumberFormatException e) {

        }

        String message= request.getParameter("message");

        Order currentOrder=null;
        HttpSession session=request.getSession();
        List<Order> cartOrderList = (List<Order>) session.getAttribute("cart");
        Iterator<Order> orderIterator = cartOrderList.iterator();
        while (orderIterator.hasNext()){
            Order order=orderIterator.next();
            if(order.getStore().getId()==storeId){
                currentOrder=order;
                orderIterator.remove();
                break;
            }
        }
        session.setAttribute("cart",cartOrderList);

        boolean isSuccess=false;
        if(currentOrder!=null){
            int userId=((User)session.getAttribute("user")).getId();
            Order order=new Order(userId,storeId,receiverId,message);
            int orderId=os.addOrderService(order);

            if(orderId>0){
                List<OrderItem> orderItemList =currentOrder.getOrderItemList();
                for (OrderItem orderItem:orderItemList) {
                    /*int foodId = orderItem.getFood().getId();*/
                    Food food=orderItem.getFood();
                    int number=orderItem.getNumber();
                    isSuccess=os.addOrderItemService(new OrderItem(food.getId(),orderId,
                            number,userId,number*food.getPrice()));
                    if(isSuccess){
                        //更新商品的销量和库存，更新商家的销量
                        if(!fs.updateFoodSalesService(number,food.getId()) || !ss.updateSalesService(number,storeId)) {
                            request.setAttribute("Order","下单失败");
                            request.getRequestDispatcher("cart.jsp").forward(request,response);
                            return;
                        }
                    }else {
                        request.setAttribute("Order","下单失败");
                        request.getRequestDispatcher("cart.jsp").forward(request,response);
                        return;
                    }
                }
                request.setAttribute("Order","下单成功");
                request.getRequestDispatcher("cart.jsp").forward(request,response);
            }else {
                request.setAttribute("Order","下单失败");
                request.getRequestDispatcher("cart.jsp").forward(request,response);

            }
        } else {
            request.setAttribute("Order","下单失败");
            request.getRequestDispatcher("cart.jsp").forward(request,response);
        }
    }
}
