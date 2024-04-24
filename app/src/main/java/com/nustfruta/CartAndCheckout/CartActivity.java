package com.nustfruta.CartAndCheckout;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Product> productArrayList;
    private ArrayAdapter<String> arrayAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        // Recyclerview initialization
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productArrayList = new ArrayList<>();

        // TODO: fill up productArrayList

        recyclerViewAdapter = new RecyclerViewAdapter(CartActivity.this, productArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);




    }
}


