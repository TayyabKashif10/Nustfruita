package com.nustfruta.CartAndCheckout;

import android.content.ClipData;

public class Product {
    private final String name;

    // TODO: Implement functionality to modify quantity and hence change price.
    public int quantity;
    public int productPricePerUnit;

    public Product(String name, int quantity, int productPricePerUnit, int image) {
        this.name = name;
        this.quantity = quantity;
        this.productPricePerUnit = productPricePerUnit;
        this.image = image;
    }

    public void increaseQuantity() {
        quantity++;
    }

    public void decreaseQuantity() {
        quantity--;
    }

    private int image;


    public int getImage() {
        return image;
    }

    public int getProductPricePerUnit() {
        return productPricePerUnit;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}

