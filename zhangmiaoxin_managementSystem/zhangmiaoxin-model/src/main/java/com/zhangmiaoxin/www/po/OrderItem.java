package com.zhangmiaoxin.www.po;

public class OrderItem {
    private int foodId;
    private int number;
    private int orderId;
    private int buyerId;
    private double price;
    private Food food;

    public OrderItem() {

    }

    public OrderItem(int foodId, int number) {
        this.foodId = foodId;
        this.number = number;
    }

    public OrderItem(int number, Food food, double price) {
        this.number = number;
        this.food = food;
        this.price=price;
    }

    public OrderItem(Food food, int number, int orderId, int buyerId, double price) {
        this.number = number;
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.price = price;
        this.food = food;
    }

    public OrderItem(int foodId, int orderId, int number, int buyerId, double price) {
        this.foodId = foodId;
        this.orderId=orderId;
        this.number = number;
        this.buyerId = buyerId;
        this.price = price;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
