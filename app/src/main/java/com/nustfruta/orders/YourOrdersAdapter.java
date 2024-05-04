package com.nustfruta.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;
import com.nustfruta.models.Order;
import com.nustfruta.utility.DateFormat;

import java.util.ArrayList;

public class YourOrdersAdapter extends RecyclerView.Adapter<YourOrdersAdapter.ViewHolder> {

    final private ArrayList<Order> orderList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvID, tvStatus, tvDate, tvTotal;

        public ViewHolder(View view) {
            super(view);

            tvID = view.findViewById(R.id.tvID);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDate = view.findViewById(R.id.tvDate);
            tvTotal = view.findViewById(R.id.tvTotal);
        }
    }

    public YourOrdersAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View cOrderCard = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.your_orders_row, viewGroup, false);

        return new ViewHolder(cOrderCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Order thisOrder = orderList.get(position);

        viewHolder.tvID.setText(Integer.toString(thisOrder.getOrderID()));
        viewHolder.tvStatus.setText(thisOrder.getStatus().toString());
        viewHolder.tvDate.setText(DateFormat.DDMMYY(thisOrder.getDateTime()));
        viewHolder.tvTotal.setText(String.format("PKR %d", thisOrder.getTotal()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
