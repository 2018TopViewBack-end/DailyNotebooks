package com.zhangmiaoxin.www.po;


public class Store {
    private int id;
    private String name;
    private String tel;
    private String address;
    private String pic;
    private int ownerId;
    private String categoryName;
    private int categoryId;
    private int sales;
    private boolean usable;

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", pic='" + pic + '\'' +
                ", ownerId=" + ownerId +
                ", category='" + categoryName + '\'' +
                ", sales=" + sales + "usable" + usable +
                '}';
    }

    public Store() {

    }

    public Store(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Store(int id, String name, String tel, String address, String pic, int ownerId, String categoryName) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.pic = pic;
        this.ownerId = ownerId;
        this.categoryName = categoryName;
    }

    public Store(String name, String tel, String address, String pic, int ownerId, int categoryId) {
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.pic = pic;
        this.ownerId = ownerId;
        this.categoryId = categoryId;
    }

    public Store(int id, String name, String picLocation) {
        this.id = id;
        this.name = name;
        this.pic = picLocation;
    }

    public Store(int id, String name, String tel, String address, String pic, String categoryName, int sales) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.pic = pic;
        this.categoryName = categoryName;
        this.sales = sales;
    }

    public Store(int id, String pic, String name, String tel, String address, int ownerId, String categoryName, int sales) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.pic = pic;
        this.ownerId = ownerId;
        this.categoryName = categoryName;
        this.sales=sales;
    }

    public Store(int id, String pic, String name, String tel, String address,
                 int ownerId, String categoryName, int sales, boolean usable) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.pic = pic;
        this.ownerId = ownerId;
        this.categoryName = categoryName;
        this.sales=sales;
        this.usable=usable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean getUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getCategory() {
        return categoryName;
    }

    public void setCategory(String categoryId) {
        this.categoryName = categoryName;
    }
}
