package com.ats.barstockexchangeuser.bean;

import java.util.List;

/**
 * Created by maxadmin on 27/11/17.
 */

public class OrdersByUser {

    private Integer userId;
    private String username;
    private String mobile;
    private List<OrderDisplay> orderDisplay;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<OrderDisplay> getOrderDisplay() {
        return orderDisplay;
    }

    public void setOrderDisplay(List<OrderDisplay> orderDisplay) {
        this.orderDisplay = orderDisplay;
    }

    @Override
    public String toString() {
        return "OrdersByUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", orderDisplay=" + orderDisplay +
                '}';
    }
}
