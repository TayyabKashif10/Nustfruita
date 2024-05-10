package com.nustfruta.utility;

import com.nustfruta.models.CartProduct;

import java.util.ArrayList;

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

    static public String[][] parseProductData(String productData) {
        String[] unsplitProducts = productData.split("\\|");
        String[][] splitProducts = new String[unsplitProducts.length][];

        for (int i = 0; i < unsplitProducts.length; i++)
            splitProducts[i] = unsplitProducts[i].split("~");

        return splitProducts;
    }

    static public int getSubtotal(String[][] products)  {
        int subtotal = 0;
        for (String[] product : products)
            subtotal += Integer.parseInt(product[1]) * Integer.parseInt(product[2]);
        return subtotal;
    }
}