package com.zhangmiaoxin.www.po;


public class Food {
    private int id;
    private String name;
    private double price;
    private int stock;
    private String pic;
    private String description;
    private int storeId;
    private String storeName;
    private int sales;
    private Store store;
    private boolean usable;

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", pic='" + pic + '\'' +
                ", description='" + description + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", sales=" + sales +
                ", store=" + store +
                ", usable=" + usable +
                '}';
    }

    public Food() {

    }

    public Food(int id, String name, double price, String pic, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pic = pic;
        this.description = description;
    }

    public Food(int id, String name, double price, String pic, String description, int storeId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pic = pic;
        this.description = description;
        this.storeId = storeId;
    }

    public Food(int id, String name, double price, String pic, String description, Store store) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pic = pic;
        this.description = description;
        this.store=store;
    }

    public Food(int id, String name, double price, int stock, String pic, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.pic = pic;
        this.description = description;
    }


    public Food(String name, double price, String pic, String description, int storeId) {
        this.name = name;
        this.price = price;
        this.pic = pic;
        this.description = description;
        this.storeId = storeId;
    }

    public Food(String name, double price, String pic) {
        this.name = name;
        this.price = price;
        this.pic = pic;
    }

    public Food(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Food(int id, String name, double price, int storeId, String storeName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.storeId=storeId;
        this.storeName=storeName;
    }

    public Food(int id, String name, double price, int storeId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.storeId=storeId;
    }

    public Food(int id, String pic, String name, double price, String description, int stock, int storeId, int sales) {
        this.id=id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.pic = pic;
        this.description=description;
        this.storeId=storeId;
        this.sales=sales;
    }

    public Food(int id, String name, double price, int stock,String pic,
                int storeId, boolean usable,  String description,int sales) {
        this.id=id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.pic = pic;
        this.description=description;
        this.storeId=storeId;
        this.sales=sales;
        this.usable=usable;
    }

    public Food(int id, String name, double price, String pic) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pic = pic;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
