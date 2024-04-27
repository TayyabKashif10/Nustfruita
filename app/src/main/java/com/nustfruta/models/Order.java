package com.nustfruta.models;

import java.time.LocalDateTime;

public class Order {
    private int orderID;
    private LocalDateTime date;
    private User user;
    private OrderStatus status;

    Order(int orderID, LocalDateTime date, User user, OrderStatus status) {
        this.orderID = orderID;
        this.date = date;
        this.user = user;
        this.status = status;
    }

    Order() {
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
