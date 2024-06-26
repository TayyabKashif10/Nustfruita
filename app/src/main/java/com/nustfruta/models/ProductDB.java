package com.nustfruta.models;

public class ProductDB {

    private String productName;

    private String productUnit;

    private int unitPrice;

    private String imageURL;

    public ProductDB(String productName, String productUnit, int unitPrice, String imageURL) {
        this.productName = productName;
        this.productUnit = productUnit;
        this.unitPrice = unitPrice;
        this.imageURL = imageURL;
    }

    public ProductDB() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
