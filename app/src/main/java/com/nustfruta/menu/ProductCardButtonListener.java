package com.nustfruta.menu;

import com.nustfruta.models.ProductDB;

public interface ProductCardButtonListener {

    public void addObject(ProductDB productDB);

    public void deleteObject(int position, String completeImageURL);

}
