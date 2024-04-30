package com.nustfruta.CartAndCheckout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;
import com.nustfruta.models.Product;

import java.util.ArrayList;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<com.nustfruta.CartAndCheckout.CartActivity.ViewHolder> {
    private final Context context;
    public ArrayList<Product> productArrayList;

    public CartActivity.ViewHolder viewHolder;

    public CartRecyclerViewAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    // Where to get the single card as view holder object
    @NonNull
    @Override
    public CartActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 1) {
            View view = inflater.inflate(R.layout.explore_menu_row, parent, false);
            viewHolder = new CartActivity.ViewHolder(view, viewType);
            return viewHolder;
        }


        View view = inflater.inflate(R.layout.cart_activity_row, parent, false);
        viewHolder = new CartActivity.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartActivity.ViewHolder holder, int position) {

        if (getItemViewType(position) == 0) {
            Product product = productArrayList.get(position);
            holder.productName.setText(product.getName());
            holder.price.setText(Integer.toString(product.getQuantity() * product.getUnitPrice()));
            holder.quantity.setText(Integer.toString(product.getQuantity()));
            holder.productIcon.setImageResource(productArrayList.get(position).getImage());
        }
    }

    // How many items
    @Override
    public int getItemCount() {
        return productArrayList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == productArrayList.size())
            return 1;
        else
            return 0;
    }
}
