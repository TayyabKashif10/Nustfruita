package com.nustfruta.CartAndCheckout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nustfruta.R;
import com.nustfruta.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    int[] productImages = {}; //TODO: INITIALIZE WITH R.drawable.

    public static ArrayList<Product> productArrayList;
    public static CartRecyclerViewAdapter cartRecyclerViewAdapter;


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

        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(CartActivity.this, productArrayList);
        recyclerView.setAdapter(cartRecyclerViewAdapter);





    }

    // to avoid memory leak
    public void onDestroy() {
        super.onDestroy();
        cartRecyclerViewAdapter = null;
    }

    private void initProductArrayList() {
        productArrayList.add(new Product(1, 200, "Banana", 10, R.drawable.banana));
        productArrayList.add(new Product(1, 500, "Strawberry", 50, R.drawable.banana));
        productArrayList.add(new Product(1, 300, "Guava", 5, R.drawable.banana));
        productArrayList.add(new Product(1, 100, "Pear", 2, R.drawable.banana));
        productArrayList.add(new Product(1, 50, "Apple", 8, R.drawable.banana));
    }


}

