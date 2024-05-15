package com.nustfruta.menu_fragments;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nustfruta.R;
import com.nustfruta.menu.ProductCardButtonListener;
import com.nustfruta.models.ProductDB;
import com.nustfruta.models.UserType;
import com.nustfruta.utility.FirebaseStorageUtil;

public class ProductAdapter extends FirebaseRecyclerAdapter<ProductDB, ProductAdapter.ProductHolder> {

    UserType userType;

    Context parentContext;
    public ProductCardButtonListener productCardButtonListener;
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<ProductDB> options, ProductCardButtonListener modifier, UserType userType, Context parentContext) {
        super(options);
        this.productCardButtonListener = modifier;
        this.userType = userType;
        this.parentContext = parentContext;
    }

     public static class ProductHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice;
        ImageView productImage;
        Button addButton;
        ImageButton deleteButton;
        public ProductHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.product_name);
            productPrice = view.findViewById(R.id.product_price);
            productImage = view.findViewById(R.id.product_image);
            addButton = view.findViewById(R.id.addButton);
            deleteButton = view.findViewById(R.id.deleteButton);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder productHolder, int i, @NonNull ProductDB productDB)
    {
        productHolder.productPrice.setText( "PKR " + productDB.getUnitPrice());
        productHolder.productName.setText(productDB.getProductName());

        FirebaseStorageUtil.BindImage(productHolder.productImage, productDB.getImageURL(), parentContext);

        if (userType == UserType.ADMIN)
        {
            productHolder.addButton.setVisibility(View.INVISIBLE);
            productHolder.deleteButton.setVisibility(View.VISIBLE);
        }
        else
        {
            productHolder.addButton.setVisibility(View.VISIBLE);
            productHolder.deleteButton.setVisibility(View.INVISIBLE);
        }
        productHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productCardButtonListener.addObject(productDB);
            }
        });

        productHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productCardButtonListener.deleteObject(i, productDB.getImageURL());
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
