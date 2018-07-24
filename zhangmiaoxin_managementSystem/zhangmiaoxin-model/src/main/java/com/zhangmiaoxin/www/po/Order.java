package com.zhangmiaoxin.www.po;

import java.util.List;

public class Order {
    private int id;
    private int userId;
    private int storeId;
    private List<Food> foodList;
    private List<Integer> foodIdList;
    private double price;
    private int receiverId;
    private String message;
    private String date;
    private String status;
    private Receiver receiver;
    private Store store;
    private List<OrderItem> orderItemList;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", storeId=" + storeId +
                ", foodList=" + foodList +
                ", foodIdList=" + foodIdList +
                ", price=" + price +
                ", receiverId=" + receiverId +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", receiver=" + receiver +
                ", store=" + store +
                ", orderItemList=" + orderItemList +
                '}';
    }

    public Order() {

    }

    public Order(int userId, int storeId, int receiverId, String message) {
        this.userId = userId;
        this.storeId = storeId;
        this.receiverId = receiverId;
        this.message = message;
    }

    public Order(Store store, List<OrderItem> orderItemList) {
        this.store = store;
        this.orderItemList = orderItemList;
    }

    public Order(int id, String message, String date, String status) {
        this.id=id;
        this.message = message;
        this.date = date;
        this.status = status;
    }

    public Order(int userId, int storeId, double price, int receiverId, String message, String date, String status) {
        this.userId = userId;
        this.storeId=storeId;
        this.price = price;
        this.receiverId = receiverId;
        this.message = message;
        this.date = date;
        this.status = status;
    }

    public Order(int userId, List<Food> foodList, int receiverId, String message,
                 String date, String status) {
        this.userId = userId;
        this.foodList = foodList;

        double price=0.0;
        for (Food food:foodList) {
            price+=food.getPrice();
        }
        this.price=price;
        this.receiverId=receiverId;
        this.message = message;
        this.date = date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public List<Integer> getFoodIdList() {
        return foodIdList;
    }

    public void setFoodIdList(List<Integer> foodIdList) {
        this.foodIdList = foodIdList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Food> foodList) {
        this.foodList = foodList;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
