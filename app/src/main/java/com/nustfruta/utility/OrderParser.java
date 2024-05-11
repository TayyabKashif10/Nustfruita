package com.nustfruta.utility;

import com.nustfruta.models.CartProduct;

import java.util.ArrayList;
import java.util.Currency;

abstract public class OrderParser {


    static public String getProductDataString(ArrayList<CartProduct> productArray)
    {
        StringBuilder productData = new StringBuilder();
        String currentProductData;

        for (CartProduct product: productArray)
        {
            currentProductData = product.getProductName() + "~" + product.getQuantity() + "~" + product.getUnitPrice() + "|";
            productData.append(currentProductData);
        }

        productData.deleteCharAt(productData.length()-1);

        return productData.toString();
    }






}
