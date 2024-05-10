package com.nustfruta.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;

public class OrderTrackingAdapter extends RecyclerView.Adapter<OrderTrackingAdapter.ViewHolder> {

    OrderTrackingActivity parent;
    final private String[][] productData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvProductName;
        final TextView tvProductPrice;

        public ViewHolder(View view) {
            super(view);

            tvProductName = view.findViewById(R.id.tvProductName);
            tvProductPrice = view.findViewById(R.id.tvProductPrice);
        }
    }

    public OrderTrackingAdapter(OrderTrackingActivity parent, String[][] productData) {
        this.productData = productData;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View llProductRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_text_row, viewGroup, false);

        llProductRow.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llProductRow.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                parent.setRowHeight(llProductRow.getHeight());
            }
        });

        return new ViewHolder(llProductRow);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        String[] thisProduct = productData[position];

        viewHolder.tvProductName.setText(thisProduct[0] + " x " + thisProduct[1]);
        viewHolder.tvProductPrice.setText(Integer.toString(Integer.parseInt(thisProduct[1]) * Integer.parseInt(thisProduct[2])));
    }

    @Override
    public int getItemCount() {
        return productData.length;
    }
}
