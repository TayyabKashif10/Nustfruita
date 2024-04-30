package com.nustfruta.CartAndCheckout;

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
import com.nustfruta.models.Product;

import java.util.ArrayList;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, price, quantity, subtotal, subtotalPrice, delivery,
                deliveryPrice, dashedLine, total, totalPrice;
        public ImageView productIcon;
        public FloatingActionButton plusButton, minusButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.quantity);
            productIcon = itemView.findViewById(R.id.productIcon);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    minusButton.setImageResource(R.drawable.minus_icon);
                    CartActivity.productArrayList.get(getAdapterPosition()).incrementQuantity();
                    CartActivity.cartRecyclerViewAdapter.notifyItemChanged(getAdapterPosition());
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartActivity.productArrayList.get(getAdapterPosition()).decrementQuantity();

                    if (CartActivity.productArrayList.get(getAdapterPosition()).getQuantity() == 0) {
                        CartActivity.productArrayList.remove(getAdapterPosition());
                        CartActivity.cartRecyclerViewAdapter.notifyItemRemoved(getAdapterPosition());
                    } else
                        CartActivity.cartRecyclerViewAdapter.notifyItemChanged(getAdapterPosition());
                }
            });
        }


        public ViewHolder(@NonNull View itemView, int itemViewType) {
            super(itemView);
            subtotal = itemView.findViewById(R.id.subtotal);
            subtotalPrice = itemView.findViewById(R.id.subtotalPrice);
            delivery = itemView.findViewById(R.id.delivery);
            deliveryPrice = itemView.findViewById(R.id.deliveryPrice);
            dashedLine = itemView.findViewById(R.id.lineDivider);
            total = itemView.findViewById(R.id.total);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }

    }


    private final Context context;
    public ArrayList<Product> productArrayList;

    public CartRecyclerViewAdapter.ViewHolder viewHolder;

    public CartRecyclerViewAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    // Where to get the single card as view holder object
    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == 1) {
            View view = inflater.inflate(R.layout.explore_menu_row, parent, false);
            viewHolder = new CartActivity.ViewHolder(view, viewType);
            return viewHolder;
        }


        View view = inflater.inflate(R.layout.cart_activity_row, parent, false);
        viewHolder = new CartRecyclerViewAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {

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
