package com.nustfruta.CartAndCheckout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.nustfruta.R;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {


    int[] productImages = {}; //TODO: INITIALIZE WITH R.drawable.

    public static ArrayList<Product> productArrayList;
    static RecyclerViewAdapter recyclerViewAdapter;

    public static Button checkoutButton;

    public static String deliveryNotesString;

    public static EditText deliveryNotes;

    public static TextView checkoutPrice, checkoutItems;

    public static int subtotal = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        // Recyclerview initialization
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productArrayList = new ArrayList<>();

        initProductArrayList();

        recyclerViewAdapter = new RecyclerViewAdapter(CartActivity.this, productArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutPrice = findViewById(R.id.checkoutPrice);
        checkoutItems = findViewById(R.id.checkoutItems);
        checkoutItems.setText(String.format("%d items", productArrayList.size()));

        for(int i = 0; i < productArrayList.size(); i++)
        {
            subtotal += productArrayList.get(i).getQuantity() * productArrayList.get(i).getProductPricePerUnit();
        }

        checkoutPrice.setText("Rs. " + (subtotal + 50));

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryNotesString = String.valueOf(deliveryNotes.getText());

                // TODO: transition to next activity using intent
            }
        });


    }

    // to avoid memory leak
    public void onDestroy() {
        super.onDestroy();
        recyclerViewAdapter = null;
        deliveryNotes = null;
        checkoutButton = null;
        checkoutPrice = null;
        checkoutItems = null;
    }

    private void initProductArrayList() {
        productArrayList.add(new Product("test1", 2, 100, R.drawable.banana_icon));
        productArrayList.add(new Product("test2", 50, 50, R.drawable.banana_icon));
        productArrayList.add(new Product("test3", 5, 210, R.drawable.banana_icon));
        productArrayList.add(new Product("test4", 9, 520, R.drawable.banana_icon));
        productArrayList.add(new Product("test5", 6, 10, R.drawable.banana_icon));
        productArrayList.add(new Product("test6", 1, 25, R.drawable.banana));
        productArrayList.add(new Product("test7", 5, 30, R.drawable.banana));

    }


    static class RecyclerViewAdapter extends RecyclerView.Adapter<com.nustfruta.CartAndCheckout.CartActivity.ViewHolder> {


        private final Context context;
        public ArrayList<Product> productArrayList;

        public ViewHolder viewHolder;

        public boolean initialStage = true;

        public RecyclerViewAdapter(Context context, ArrayList<Product> productArrayList) {
            this.context = context;
            this.productArrayList = productArrayList;
        }

        // Where to get the single card as view holder object
        @NonNull
        @Override
        public CartActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(context);

            if (viewType == 1) {
                View view = inflater.inflate(R.layout.total_costs_row, parent, false);
                viewHolder = new CartActivity.ViewHolder(view, viewType);
                return viewHolder;
            }

            if(viewType == 2) {
                View view = inflater.inflate(R.layout.last_product_row, parent, false);
                viewHolder = new CartActivity.ViewHolder(view, viewType);
                return viewHolder;
            }

            View view = inflater.inflate(R.layout.cart_activity_row, parent, false);
            viewHolder = new CartActivity.ViewHolder(view, viewType);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull CartActivity.ViewHolder holder, int position) {

            if (getItemViewType(position) != 1)
            {
                Product product = productArrayList.get(position);
                holder.productName.setText(product.getName());
                holder.price.setText("Rs. " + Integer.toString(product.getQuantity() * product.getProductPricePerUnit()));
                holder.quantity.setText(Integer.toString(product.getQuantity()));
                holder.productIcon.setImageResource(productArrayList.get(position).getImage());
            }

            else
            {
                initialStage = false;
                holder.subtotalPrice.setText("Rs. " + (subtotal));
                holder.totalPrice.setText("Rs. " + (subtotal + 50));
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
            else if (position == productArrayList.size() - 1) {
                return 2;
            } else
                return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, price, quantity, subtotal, subtotalPrice, delivery,
                deliveryPrice, total, totalPrice;
        public ImageView productIcon, plusButton, minusButton;
        public View lineDivider, lastLineDivider;


        public ViewHolder(@NonNull View itemView, int itemViewType) {
            super(itemView);

            if (itemViewType == 0)
            {
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
                        CartActivity.productArrayList.get(getBindingAdapterPosition()).increaseQuantity();

                        CartActivity.subtotal += productArrayList.get(getBindingAdapterPosition()).getProductPricePerUnit();
                        checkoutPrice.setText("Rs. " + (CartActivity.subtotal + 50));

                        CartActivity.recyclerViewAdapter.notifyItemChanged(getBindingAdapterPosition());
                        CartActivity.recyclerViewAdapter.notifyItemChanged(productArrayList.size());
                    }
                });

                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartActivity.productArrayList.get(getBindingAdapterPosition()).decreaseQuantity();

                        CartActivity.subtotal -= productArrayList.get(getBindingAdapterPosition()).getProductPricePerUnit();

                        checkoutPrice.setText("Rs. " + (CartActivity.subtotal + 50));

                        CartActivity.recyclerViewAdapter.notifyItemChanged(productArrayList.size());


                        if (CartActivity.productArrayList.get(getBindingAdapterPosition()).getQuantity() == 0)
                        {
                            CartActivity.productArrayList.remove(getBindingAdapterPosition());
                            CartActivity.recyclerViewAdapter.notifyItemRemoved(getBindingAdapterPosition());
                            checkoutItems.setText(productArrayList.size() + " items");
                        }
                        else
                        {
                            CartActivity.recyclerViewAdapter.notifyItemChanged(getBindingAdapterPosition());
                        }
                    }
                });
            }
            else if (itemViewType == 1)
            {
                subtotal = itemView.findViewById(R.id.subtotal);
                subtotalPrice = itemView.findViewById(R.id.subtotalPrice);
                delivery = itemView.findViewById(R.id.delivery);
                deliveryPrice = itemView.findViewById(R.id.deliveryPrice);
                lineDivider = itemView.findViewById(R.id.lineDivider);
                total = itemView.findViewById(R.id.total);
                totalPrice = itemView.findViewById(R.id.totalPrice);
            }
            else
            {
                productName = itemView.findViewById(R.id.lastProductName);
                price = itemView.findViewById(R.id.lastProductPrice);
                quantity = itemView.findViewById(R.id.lastQuantity);
                productIcon = itemView.findViewById(R.id.lastProductIcon);
                plusButton = itemView.findViewById(R.id.lastPlusButton);
                minusButton = itemView.findViewById(R.id.lastMinusButton);
                lastLineDivider = itemView.findViewById(R.id.lastLineDivider);
                deliveryNotes = itemView.findViewById(R.id.deliveryNotes);

                plusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartActivity.productArrayList.get(getBindingAdapterPosition()).increaseQuantity();

                        CartActivity.subtotal += productArrayList.get(getBindingAdapterPosition()).getProductPricePerUnit();

                        checkoutPrice.setText("Rs. " + (CartActivity.subtotal + 50));

                        CartActivity.recyclerViewAdapter.notifyItemChanged(getBindingAdapterPosition());
                        CartActivity.recyclerViewAdapter.notifyItemChanged(productArrayList.size());
                    }
                });

                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CartActivity.productArrayList.get(getBindingAdapterPosition()).decreaseQuantity();

                        CartActivity.subtotal -= productArrayList.get(getBindingAdapterPosition()).getProductPricePerUnit();

                        CartActivity.recyclerViewAdapter.notifyItemChanged(productArrayList.size());

                        checkoutPrice.setText("Rs. " + (CartActivity.subtotal + 50));

                        if (CartActivity.productArrayList.get(getBindingAdapterPosition()).getQuantity() == 0)
                        {
                            CartActivity.productArrayList.remove(getBindingAdapterPosition());
                            CartActivity.recyclerViewAdapter.notifyItemRemoved(getBindingAdapterPosition());
                            checkoutItems.setText(String.format("%d items", productArrayList.size()));
                        }
                        else
                        {
                            CartActivity.recyclerViewAdapter.notifyItemChanged(getBindingAdapterPosition());
                        }


                    }
                });

            }
        }


    }




}

