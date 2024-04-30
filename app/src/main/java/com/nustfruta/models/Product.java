package com.nustfruta.models;

public class Product {
    private int productID, unitPrice, quantity;
    private String name;

    private int image;

    public Product(int productID, int unitPrice, String name, int quantity, int image) {
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }

    public Product() {
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
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

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        quantity--;
    }

    public int getImage() {
        return image;
    }
}
