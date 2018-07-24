package com.zhangmiaoxin.www.po;


public class Receiver {
    private int id;
    private int userId;
    private String name;
    private String tel;
    private String address;

    @Override
    public String toString() {
        return "Receiver{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Receiver() {

    }

    public Receiver(String name, String tel, String address) {
        this.name = name;
        this.tel = tel;
        this.address = address;
    }

    public Receiver(int id, String name, String tel, String address) {
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
    }

    public Receiver(String name, String tel, String address, int userId) {
        this.userId = userId;
        this.name = name;
        this.tel = tel;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
