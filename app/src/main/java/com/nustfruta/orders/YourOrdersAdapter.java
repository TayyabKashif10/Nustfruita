package com.nustfruta.orders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nustfruta.R;
import com.nustfruta.models.OrderDB;
import com.nustfruta.utility.OrderParser;

public class YourOrdersAdapter extends FirebaseRecyclerAdapter<OrderDB, YourOrdersAdapter.ViewHolder> {

    YourOrdersActivity parent;

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

    public YourOrdersAdapter(YourOrdersActivity parent, FirebaseRecyclerOptions options) {
        super(options);
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d("create", "created");
        View cOrderCard = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.your_orders_row, viewGroup, false);

        return new ViewHolder(cOrderCard);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position, OrderDB order) {
        Log.d("Bind", order.getProductData());

        viewHolder.tvID.setText(order.getOrderID());
        viewHolder.tvStatus.setText(order.getStatus().toString());
        viewHolder.tvDate.setText(order.getDateTime());
        viewHolder.tvTotal.setText(String.format("PKR %d", OrderParser.getSubtotal(OrderParser.parseProductData(order.getProductData()))));
        viewHolder.cMainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.expandCard(order.getOrderID());
            }
        });
    }
}