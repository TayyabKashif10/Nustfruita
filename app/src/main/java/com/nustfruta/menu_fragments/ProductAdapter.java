package com.nustfruta.menu_fragments;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.nustfruta.R;
import com.nustfruta.menu.ArrayModifier;
import com.nustfruta.models.ProductDB;
import com.nustfruta.utility.Constants;
import com.nustfruta.utility.FirebaseStorageUtil;

public class ProductAdapter extends FirebaseRecyclerAdapter<ProductDB, ProductAdapter.ProductHolder> {

    public ArrayModifier arrayModifier;
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<ProductDB> options, ArrayModifier modifier) {
        super(options);
        this.arrayModifier = modifier;
    }

     public static class ProductHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        ImageView productImage;
        Button addButton;
        public ProductHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.product_name);
            productPrice = view.findViewById(R.id.product_price);
            productImage = view.findViewById(R.id.product_image);
            addButton = view.findViewById(R.id.addButton);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder productHolder, int i, @NonNull ProductDB productDB)
    {
        productHolder.productPrice.setText( "PKR " + productDB.getUnitPrice());
        productHolder.productName.setText(productDB.getProductName());

        //Glide.with(productHolder.productImage.getContext()).load(productDB.getImageURL()).placeholder(R.drawable.fruit_apple).error(R.drawable.fruit_pear).into(productHolder.productImage);

        FirebaseStorageUtil.BindImage(productHolder.productImage, productDB.getImageURL());

        productHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayModifier.addObject(productDB);
                Snackbar.make(v,"Added to Cart.",Snackbar.LENGTH_SHORT).setBackgroundTint(Constants.COLOR_PRIMARY).show();
            }
        });

    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_layout, parent, false);

        return new ProductHolder(view);
    }
}
