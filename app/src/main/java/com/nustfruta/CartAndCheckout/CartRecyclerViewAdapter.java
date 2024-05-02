package com.nustfruta.CartAndCheckout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nustfruta.R;
import com.nustfruta.models.Product;

import java.util.ArrayList;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {


    CartActivity parent;

    ArrayList<Product> productList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, price, quantity;
        public ImageView productIcon;
        public ImageButton plusButton, minusButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            productIcon = itemView.findViewById(R.id.productIcon);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);

        }
    }


    public CartRecyclerViewAdapter(CartActivity parent, ArrayList<Product> productList) {

        this.parent = parent;
        this.productList = productList;
    }

    // Where to get the single card as view holder object
    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cart_activity_row, viewGroup, false);
        return new CartRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {

            Product product = productList.get(position);
            holder.productName.setText(product.getName());
            holder.price.setText("PKR " + Integer.toString(product.getQuantity() * product.getUnitPrice()));
            holder.quantity.setText(Integer.toString(product.getQuantity()));
            holder.productIcon.setImageResource(productList.get(position).getImage());


        // onClick listener of plus buttons
        holder.plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.plusButton(holder.getBindingAdapterPosition());
            }
        });

        // onClick listener of minus button
        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.minusButton(holder.getBindingAdapterPosition());
            }
        });

    }

    // How many items
    @Override
    public int getItemCount() {
        return productList.size();
    }
}
