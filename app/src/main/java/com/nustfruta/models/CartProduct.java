package com.nustfruta.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


//TODO: make parcelable
// send snapshot of arraylist from viewmodel to cart activity
// use activity for result to get changed arraylist from cart activity back in menu activity
// update the viewmodel according to the result from cart activity
// instead of extending productDB, give same fields, but make unitPrice integer and shit.

public class CartProduct implements Parcelable
{

    private int quantity;

    private String productName;

    private String productUnit;

    private int unitPrice;

    private String imageURL;

    public CartProduct() {
    }

    public CartProduct(int quantity, String productName, String productUnit, int unitPrice, String imageURL) {
        this.quantity = quantity;
        this.productName = productName;
        this.productUnit = productUnit;
        this.unitPrice = unitPrice;
        this.imageURL = imageURL;
    }

    public CartProduct(ProductDB productDB, int quantity) {
        this.productName = productDB.getProductName();
        this.productUnit = productDB.getProductUnit();
        this.unitPrice = productDB.getUnitPrice();
        this.imageURL = productDB.getImageURL();
        this.quantity = quantity;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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


    public void incrementQuantity()
    {
        quantity++;
    }

    public void decrementQuantity()
    {
        quantity--;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(quantity);
        dest.writeString(productName);
        dest.writeString(productUnit);
        dest.writeInt(unitPrice);
        dest.writeString(imageURL);
    }


    public static final Creator<CartProduct> CREATOR = new Creator<CartProduct>() {
        @Override
        public CartProduct createFromParcel(Parcel in) {
            return new CartProduct(in);
        }

        @Override
        public CartProduct[] newArray(int size) {
            return new CartProduct[size];
        }
    };

    protected CartProduct(Parcel in) {

        this.quantity = in.readInt();
        this.productName = in.readString();
        this.productUnit = in.readString();
        this.unitPrice = in.readInt();
        this.imageURL = in.readString();

    }

}
