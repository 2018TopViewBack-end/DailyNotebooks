package com.zhangmiaoxin.www.po;

public class User {
    private int id;
    private String username;
    private String password;
    private String tel;
    private String pic;
    private String name;
    private int roleId;
    private String role;

    public User() {

    }

    public User(int id, String password) {
        this.id = id;
        this.password = password;
    }

    public User(String username, String tel, String name) {
        this.username = username;
        this.tel=tel;
        this.name = name;
    }

    public User(String username, String password, int roleId) {
        this.username = username;
        this.password = password;
        this.roleId = roleId;
    }

    public User(int id, String tel, String pic, String name) {
        this.id = id;
        this.tel = tel;
        this.pic = pic;
        this.name = name;
    }

    public User(String username, String password, int roleId, String role) {
        this.username = username;
        this.password = password;
        this.roleId=roleId;
        this.role=role;
    }

    public User(String username, String password, String tel, String name) {
        this.username = username;
        this.password = password;
        this.tel = tel;
        this.name = name;
    }

    public User(int id, String username, String password, String tel, String pic, String name, int roleId, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.tel = tel;
        this.pic = pic;
        this.name = name;
        this.roleId = roleId;
        this.role=role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
