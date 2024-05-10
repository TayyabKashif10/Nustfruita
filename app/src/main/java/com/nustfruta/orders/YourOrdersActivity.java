package com.nustfruta.orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.nustfruta.R;
import com.nustfruta.models.Order;
import com.nustfruta.models.OrderDB;
import com.nustfruta.models.OrderStatus;
import com.nustfruta.models.LegacyProduct;
import com.nustfruta.models.ProductDB;
import com.nustfruta.models.User;
import com.nustfruta.utility.FirebaseDBUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class YourOrdersActivity extends AppCompatActivity {

    FirebaseRecyclerOptions options;
    RecyclerView rvOrderList;
    YourOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeOrderList();
    }

    private void initializeOrderList() {
        rvOrderList = findViewById(R.id.rvOrderList);
        options = new FirebaseRecyclerOptions.Builder<OrderDB>().setQuery(FirebaseDatabase.getInstance().getReference().child("orders"), OrderDB.class).build();

        adapter = new YourOrdersAdapter(this, options);

        rvOrderList.setLayoutManager(new LinearLayoutManager(this));
        rvOrderList.setAdapter(adapter);
    }


    public void expandCard(String orderID)
    {
        Intent intent = new Intent(this, OrderTrackingActivity.class);
        intent.putExtra("ID", orderID);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}