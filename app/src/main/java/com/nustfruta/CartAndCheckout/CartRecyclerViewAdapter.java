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
                deliveryPrice, total, totalPrice;

        public View dashedLine;
        public ImageView productIcon, plusButton, minusButton;


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
                    CartActivity.productArrayList.get(getBindingAdapterPosition()).incrementQuantity();
                    CartActivity.cartRecyclerViewAdapter.notifyItemChanged(getBindingAdapterPosition());
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CartActivity.productArrayList.get(getBindingAdapterPosition()).decrementQuantity();

                    if (CartActivity.productArrayList.get(getBindingAdapterPosition()).getQuantity() == 0) {
                        CartActivity.productArrayList.remove(getBindingAdapterPosition());
                        CartActivity.cartRecyclerViewAdapter.notifyItemRemoved(getBindingAdapterPosition());
                    } else
                        CartActivity.cartRecyclerViewAdapter.notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }


        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
//            subtotal = itemView.findViewById(R.id.subtotal);
//            subtotalPrice = itemView.findViewById(R.id.subtotalPrice);
//            delivery = itemView.findViewById(R.id.delivery);
//            deliveryPrice = itemView.findViewById(R.id.deliveryPrice);
//            dashedLine = itemView.findViewById(R.id.lineDivider);
//            total = itemView.findViewById(R.id.total);
//            totalPrice = itemView.findViewById(R.id.totalPrice);




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
            View view = inflater.inflate(R.layout.last_product_row, parent, false);
            viewHolder = new CartRecyclerViewAdapter.ViewHolder(view, viewType);
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
        return productArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == productArrayList.size() - 1)
            return 1;
        else
            return 0;
    }
}
