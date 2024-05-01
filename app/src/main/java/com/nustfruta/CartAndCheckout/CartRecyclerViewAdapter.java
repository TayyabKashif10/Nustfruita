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

            // onClick listener of plus buttons
            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartActivity.plusButton(getBindingAdapterPosition());
                }
            });


            // onClick listener of minus button
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartActivity.minusButton(getBindingAdapterPosition());
                }
            });

        }
    }


    public final Context context;

    // Used for onClick listeners of plus and minus buttons
    public static CartActivity cartActivity = new CartActivity();

    public static CartRecyclerViewAdapter.ViewHolder viewHolder;

    public CartRecyclerViewAdapter(Context context) {

        this.context = context;
    }

    // Where to get the single card as view holder object
    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_activity_row, parent, false);
        viewHolder = new CartRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {

            Product product = CartActivity.productArrayList.get(position);
            holder.productName.setText(product.getName());
            holder.price.setText("Rs. " + Integer.toString(product.getQuantity() * product.getUnitPrice()));
            holder.quantity.setText(Integer.toString(product.getQuantity()));
            holder.productIcon.setImageResource(CartActivity.productArrayList.get(position).getImage());

    }

    // How many items
    @Override
    public int getItemCount() {
        return CartActivity.productArrayList.size();
    }
}
