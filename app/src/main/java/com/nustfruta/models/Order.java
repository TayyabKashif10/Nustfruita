package com.nustfruta.models;

import java.util.Calendar;
import java.util.ArrayList;

public class Order {
    private int orderID;
    private ArrayList<Product> productList;
    private Calendar dateTime, estDateTime;
    private User user;
    private OrderStatus status;

    public Order(int orderID, Calendar dateTime, Calendar estDateTime, User user, OrderStatus status, ArrayList<Product> productList) {
        this.orderID = orderID;
        this.dateTime = dateTime;
        this.estDateTime = estDateTime;
        this.user = user;
        this.status = status;
        this.productList = productList;
    }

    // requirement for firebase implementation
    public Order() {
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public Calendar getEstDateTime() {
        return estDateTime;
    }

    public void setEstDateTime(Calendar estDateTime) {
        this.estDateTime = estDateTime;
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

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
}
