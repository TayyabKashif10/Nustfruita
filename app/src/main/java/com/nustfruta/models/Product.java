package com.nustfruta.models;

public class Product {
    private int productID, price, quantity;
    private String name;

    public Product(int productID, int price, String name, int quantity) {
        this.productID = productID;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }

    public Product() {
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
