package com.nustfruta.CartAndCheckout;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nustfruta.R;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements ItemChangedListener {


    int[] productImages = {}; //TODO: INITIALIZE WITH R.drawable.

    public static ArrayList<Product> productArrayList;
    public RecyclerViewAdapter recyclerViewAdapter;

    public void onItemChanged(int position) {
        recyclerViewAdapter.notifyItemChanged(position);
    }

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

    private void initProductArrayList() {
        productArrayList.add(new Product("banana", 2, "22", R.drawable.banana_icon));
        productArrayList.add(new Product("apple", 50, "2441", R.drawable.banana_icon));
        productArrayList.add(new Product("tayyab bitch", 5, "234", R.drawable.banana_icon));
        productArrayList.add(new Product("afzal bitch", 9, "1233", R.drawable.banana_icon));
        productArrayList.add(new Product("cunt", 6, "20", R.drawable.banana_icon));
        productArrayList.add(new Product("bastard", 1, "102", R.drawable.banana_icon));
        productArrayList.add(new Product("nigga", 5, "22", R.drawable.banana_icon));

    }




}


