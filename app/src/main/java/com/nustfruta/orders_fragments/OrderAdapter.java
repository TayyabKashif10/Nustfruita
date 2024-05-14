package com.nustfruta.orders_fragments;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;
import com.nustfruta.models.OrderDB;
import com.nustfruta.orders.OrdersListActivity;
import com.nustfruta.orders.YourOrdersActivity;
import com.nustfruta.utility.OrderParser;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    final Activity parent;

    ArrayList<OrderDB> orderList;

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

        public void bind(Activity parent, OrderDB order) {
            tvID.setText(order.getOrderID());
            tvStatus.setText(order.getStatus().toString());
            tvDate.setText(order.getDateTime());
            tvTotal.setText(String.format("PKR %d", OrderParser.getSubtotal(OrderParser.parseProductData(order.getProductData()))));
            cMainCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (parent instanceof YourOrdersActivity) {
                        YourOrdersActivity why = (YourOrdersActivity) parent;
                        why.expandCard(order.getOrderID());
                    }
                    else if (parent instanceof OrdersListActivity) {
                        OrdersListActivity why = (OrdersListActivity) parent;
                        why.expandCard(order.getOrderID());
                    }
                }
            });
        }
    }

    public OrderAdapter(Activity parent, ArrayList<OrderDB> orderList) {
        this.orderList = orderList;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View cOrderCard = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.your_orders_row, viewGroup, false);

        return new ViewHolder(cOrderCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bind(parent, orderList.get(position));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}