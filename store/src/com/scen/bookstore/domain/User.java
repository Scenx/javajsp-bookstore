package com.scen.bookstore.domain;

import java.util.Date;

/**
 * @author Scen
 * @date 2017/11/17
 */
public class User {
    /**
     * 用户编号
     */
    private int id;
    /**
     * 用户姓名
     */

    private String username;
    /**
     * 用户密码
     */

    private String password;
    /**
     * 用户性别
     */
    private String gender;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户联系电话
     */
    private String telephone;
    /**
     * 用户介绍
     */
    private String introduce;
    /**
     * 激活码
     */
    private String activeCode;
    /**
     * 用户角色
     */
    private String role;
    /**
     * 用户状态
     */
    private int state;
    /**
     * 注册时间
     */
    private Date registTime;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }
}
