package com.nustfruta.CartAndCheckout;

import android.content.ClipData;

public class Product {
    private final String name;

    // TODO: Implement functionality to modify quantity and hence change price.
    public int quantity;
    public String stringQuantity;
    public String productPrice;

    public Product(String name, int quantity, String productPrice, int image) {
        this.name = name;
        this.quantity = quantity;
        stringQuantity = String.valueOf(quantity);
        this.productPrice = productPrice;
        this.image = image;
    }

    public void increaseQuantity() {
        quantity++;
        stringQuantity = String.valueOf(quantity);
    }

    public void decreaseQuantity() {
        quantity--;
        stringQuantity = String.valueOf(quantity);
    }

    private int image;


    public int getImage() {
        return image;
    }

    public String getStringQuantity() {
        return stringQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getName() {
        return name;
    }

}

