package com.nustfruta.menu;

import com.google.firebase.database.DatabaseReference;
import com.nustfruta.models.ProductDB;

public interface ProductCardButtonListener {

    public void addObject(ProductDB productDB);

    public void deleteObject(int position, String completeImageURL);

}
