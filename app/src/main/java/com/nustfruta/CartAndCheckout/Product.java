package com.nustfruta.CartAndCheckout;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.media.Image;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.nustfruta.R;

public class Product {
    private String name;

    private String quantity;

    private String productPrice;

    // TODO: Add vector object of the icon of the product.

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    private String quantity;
    private String productPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VectorDrawable getIcon() {
        return icon;
    }

    public void setIcon(VectorDrawable icon) {
        this.icon = icon;
    }
}

