package com.nustfruta.postorder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;
import com.nustfruta.models.Product;

import java.util.ArrayList;

public class OrderTrackingAdapter extends RecyclerView.Adapter<OrderTrackingAdapter.ViewHolder> {

    private final ArrayList<Product> productList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvProductName;
        final TextView tvProductPrice;

        public ViewHolder(View view) {
            super(view);

            tvProductName = view.findViewById(R.id.tvProductName);
            tvProductPrice = view.findViewById(R.id.tvProductPrice);
        }
    }

    public OrderTrackingAdapter(ArrayList<Product> productList) {
        this.productList = productList;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_text_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Product thisProduct = productList.get(position);
        if (thisProduct.getName().equals("..."))
            viewHolder.tvProductName.setText("...");
        else {
            viewHolder.tvProductName.setText(String.format("%s x %d", thisProduct.getName(), thisProduct.getQuantity()));
            viewHolder.tvProductPrice.setText(Integer.toString(thisProduct.getUnitPrice() * thisProduct.getQuantity()));
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}