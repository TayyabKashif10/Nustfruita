package com.nustfruta.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


//TODO: make parcelable
// send snapshot of arraylist from viewmodel to cart activity
// use activity for result to get changed arraylist from cart activity back in menu activity
// update the viewmodel according to the result from cart activity
// instead of extending productDB, give same fields, but make unitPrice integer and shit.


public class CartProduct extends ProductDB implements Parcelable
{

    private int quantity;

    public CartProduct() {
    }

    public CartProduct(String productName, String productUnit, String unitPrice, String imageURL, int quantity) {
        super(productName, productUnit, unitPrice, imageURL);
        this.quantity = quantity;
    }

    public CartProduct(ProductDB productDB, int quantity) {
        super(productDB.getProductName(), productDB.getProductUnit(), productDB.getUnitPrice(), productDB.getImageURL());
        this.quantity = quantity;
    }


    protected CartProduct(Parcel in) {
        quantity = in.readInt();
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getProductName() {
        return super.getProductName();
    }

    @Override
    public void setProductName(String productName) {
        super.setProductName(productName);
    }

    @Override
    public String getProductUnit() {
        return super.getProductUnit();
    }

    @Override
    public void setProductUnit(String productUnit) {
        super.setProductUnit(productUnit);
    }

    @Override
    public String getUnitPrice() {
        return super.getUnitPrice();
    }

    @Override
    public void setUnitPrice(String unitPrice) {
        super.setUnitPrice(unitPrice);
    }

    @Override
    public String getImageURL() {
        return super.getImageURL();
    }

    @Override
    public void setImageURL(String imageURL) {
        super.setImageURL(imageURL);
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


}
