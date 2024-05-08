package com.nustfruta.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;
import com.nustfruta.models.CartProduct;
import com.nustfruta.models.LegacyProduct;
import com.nustfruta.utility.FirebaseStorageUtil;

import java.util.ArrayList;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {


    CartActivity parent;

    ArrayList<CartProduct> productList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, price, quantity, subtotalPrice;
        public ImageView productIcon;
        public ImageButton plusButton, minusButton;


        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == 0) {
                productName = itemView.findViewById(R.id.productName);
                price = itemView.findViewById(R.id.productPrice);
                quantity = itemView.findViewById(R.id.quantity);
                productIcon = itemView.findViewById(R.id.productIcon);
                plusButton = itemView.findViewById(R.id.plusButton);
                minusButton = itemView.findViewById(R.id.minusButton);
            }

            else
                subtotalPrice = itemView.findViewById(R.id.subtotalPrice);

        }
    }


    public CartRecyclerViewAdapter(CartActivity parent, ArrayList<CartProduct> productList) {

        this.parent = parent;
        this.productList = productList;
    }

    // Where to get the single card as view holder object
    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        if(viewType == 0)
            view = inflater.inflate(R.layout.cart_activity_row, viewGroup, false);

        else
            view = inflater.inflate(R.layout.costs_row, viewGroup, false);

        return new CartRecyclerViewAdapter.ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {

        if (getItemViewType(position) == 0) {
            CartProduct product = productList.get(position);
            holder.productName.setText(product.getProductName());
            holder.price.setText("PKR " + product.getQuantity() * product.getUnitPrice());
            holder.quantity.setText(Integer.toString(product.getQuantity()));

            FirebaseStorageUtil.BindImage(holder.productIcon, productList.get(position).getImageURL());

            // onClick listener of plus buttons
            holder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.plusButton(holder, holder.getBindingAdapterPosition());
                }
            });

            // onClick listener of minus button
            holder.minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.minusButton(holder, holder.getBindingAdapterPosition());
                }
            });
        }

        else
            holder.subtotalPrice.setText("PKR " + parent.subtotal);

    }

    // How many items
    @Override
    public int getItemCount() {
        return productList.size() + 1;
    }


    public int getItemViewType(int position) {
        if(position == productList.size())
            return 1;

        return 0;
    }
}
