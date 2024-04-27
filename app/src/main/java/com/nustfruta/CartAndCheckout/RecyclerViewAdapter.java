package com.nustfruta.CartAndCheckout;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nustfruta.R;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final Context context;
    public ArrayList<Product> productArrayList;

    public RecyclerViewAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    // Where to get the single card as view holder object
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_activity_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = productArrayList.get(position);

        holder.productName.setText(product.getName());
        holder.price.setText(product.getProductPrice());
        holder.quantity.setText(product.getStringQuantity());
        holder.productIcon.setImageResource(productArrayList.get(position).getImage());
    }

    // How many items
    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemChangedListener itemChangedListener;
        public TextView productName;
        public TextView price;
        public TextView quantity;
        public ImageView productIcon;
        public FloatingActionButton plusButton, minusButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {});

            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            productIcon = itemView.findViewById(R.id.productIcon);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartActivity.productArrayList.get(getAdapterPosition()).increaseQuantity();
                    itemChangedListener.onItemChanged(getAdapterPosition());
                }
            });
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartActivity.productArrayList.get(getAdapterPosition()).decreaseQuantity();
                    itemChangedListener.onItemChanged(getAdapterPosition());

                }
            });
        }
    }
}

