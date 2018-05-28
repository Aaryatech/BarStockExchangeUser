package com.ats.barstockexchangeuser.bean;

import java.util.List;

/**
 * Created by maxadmin on 27/11/17.
 */

public class UserOrderHistory {

    private List<OrdersByUser> ordersByUser;
    private ErrorMessage errorMessage;

    public List<OrdersByUser> getOrdersByUser() {
        return ordersByUser;
    }

    public void setOrdersByUser(List<OrdersByUser> ordersByUser) {
        this.ordersByUser = ordersByUser;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "UserOrderHistory{" +
                "ordersByUser=" + ordersByUser +
                ", errorMessage=" + errorMessage +
                '}';
    }
}
