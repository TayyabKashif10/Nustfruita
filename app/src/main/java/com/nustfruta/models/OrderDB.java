package com.nustfruta.models;

import com.nustfruta.utility.DateFormat;
import com.nustfruta.utility.FirebaseDBUtil;

import java.util.Calendar;

public class OrderDB {
    private String orderID;
    private String productData;
    private String dateTime;
    private String userID;
    private OrderStatus status;

    public OrderDB(String orderID, String productData, OrderStatus status) {
        this.orderID = orderID;
        this.productData = productData;
        this.dateTime = DateFormat.EEE_DDMMYY(Calendar.getInstance());
        this.userID = FirebaseDBUtil.getCurrentUserID();
        this.status = status;
    }

    // requirement for firebase implementation
    public OrderDB() {
        this.dateTime = DateFormat.EEE_DDMMYY(Calendar.getInstance());
        this.userID = FirebaseDBUtil.getCurrentUserID();
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getProductData() {
        return productData;
    }

    public void setProductData(String productData) {
        this.productData = productData;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
