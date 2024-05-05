package com.nustfruta.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;
import com.nustfruta.models.Order;
import com.nustfruta.utility.DateFormat;

import java.util.ArrayList;

public class YourOrdersAdapter extends RecyclerView.Adapter<YourOrdersAdapter.ViewHolder> {

    final private ArrayList<Order> orderList;

    private OrderClickListener orderClickListener;

    public void setOrderClickListener(OrderClickListener orderClickListener) {
        this.orderClickListener = orderClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvID, tvStatus, tvDate, tvTotal;
        final CardView cMainCard;

        public ViewHolder(View view) {
            super(view);

            tvID = view.findViewById(R.id.tvID);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvDate = view.findViewById(R.id.tvDate);
            tvTotal = view.findViewById(R.id.tvTotal);
            cMainCard = view.findViewById(R.id.cMainCard);
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

        viewHolder.tvID.setText(thisOrder.getOrderID());
        viewHolder.tvStatus.setText(thisOrder.getStatus().toString());
        viewHolder.tvDate.setText(DateFormat.DDMMYY(thisOrder.getDateTime()));
        viewHolder.tvTotal.setText(String.format("PKR %d", thisOrder.getTotal()));

        viewHolder.cMainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderClickListener.onClick(thisOrder.getOrderID());
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
