package com.nustfruta.CartAndCheckout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nustfruta.R;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {


    int[] productImages = {}; //TODO: INITIALIZE WITH R.drawable.

    public static ArrayList<Product> productArrayList;
    public static RecyclerViewAdapter recyclerViewAdapter;



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


    }

    // to avoid memory leak
    public void onDestroy() {
        super.onDestroy();
        recyclerViewAdapter = null;
    }

    private void initProductArrayList() {
        productArrayList.add(new Product("test1", 2, "Rs. 22", R.drawable.banana_icon));
        productArrayList.add(new Product("test2", 50, "Rs. 2441", R.drawable.banana_icon));
        productArrayList.add(new Product("test3", 5, "Rs. 234", R.drawable.banana_icon));
        productArrayList.add(new Product("test4", 9, "Rs. 1233", R.drawable.banana_icon));
        productArrayList.add(new Product("test5", 6, "Rs. 20", R.drawable.banana_icon));
        productArrayList.add(new Product("testicle", 1, "Rs. 102", R.drawable.banana_icon));
        productArrayList.add(new Product("test7", 5, "Rs. 22", R.drawable.banana_icon));

    }



    static class RecyclerViewAdapter extends RecyclerView.Adapter<com.nustfruta.CartAndCheckout.CartActivity.ViewHolder> {


        private final Context context;
        public ArrayList<Product> productArrayList;

        public ViewHolder viewHolder;

        public RecyclerViewAdapter(Context context, ArrayList<Product> productArrayList) {
            this.context = context;
            this.productArrayList = productArrayList;
        }

        // Where to get the single card as view holder object
        @NonNull
        @Override
        public CartActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {

            LayoutInflater inflater = LayoutInflater.from(context);

            if(viewType == 1)
            {
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

                if(getItemViewType(position) == 0)
                {
                    Product product = productArrayList.get(position);
                    holder.productName.setText(product.getName());
                    holder.price.setText(product.getProductPrice());
                    holder.quantity.setText(product.getStringQuantity());
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
            if(position == productArrayList.size())
                return 1;
            else
                return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, price, quantity, backToMenu;
        public ImageView productIcon, arrowIcon;
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
                CartActivity.productArrayList.get(getAdapterPosition()).increaseQuantity();
                CartActivity.recyclerViewAdapter.notifyItemChanged(getAdapterPosition());
            }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                CartActivity.productArrayList.get(getAdapterPosition()).decreaseQuantity();

                if (CartActivity.productArrayList.get(getAdapterPosition()).quantity == 0) {
                    CartActivity.productArrayList.remove(getAdapterPosition());
                    CartActivity.recyclerViewAdapter.notifyItemRemoved(getAdapterPosition());
                } else
                    CartActivity.recyclerViewAdapter.notifyItemChanged(getAdapterPosition());


                }
                });
            }



        public ViewHolder(@NonNull View itemView, int itemViewType) {
            super(itemView);
            backToMenu = itemView.findViewById(R.id.explore_menu);
            arrowIcon = itemView.findViewById(R.id.right_arrow);
        }

    }




}

