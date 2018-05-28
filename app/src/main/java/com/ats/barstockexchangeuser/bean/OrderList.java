package com.ats.barstockexchangeuser.bean;

import java.util.List;

/**
 * Created by maxadmin on 27/11/17.
 */

public class OrderList {

    private Order order;
    private List<OrderDetail> orderDetails;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderList{" +
                "order=" + order +
                ", orderDetails=" + orderDetails +
                '}';
    }
}
