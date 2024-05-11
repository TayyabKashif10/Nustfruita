package com.nustfruta.orders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.nustfruta.R;
import com.nustfruta.models.OrderDB;
import com.nustfruta.utility.FirebaseDBUtil;

public class YourOrdersActivity extends AppCompatActivity {

    ImageView ivBackButton;

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
        initializeBackButton();
    }

    private void initializeOrderList() {
        rvOrderList = findViewById(R.id.rvOrderList);
        Log.d("bruh", FirebaseDBUtil.getCurrentUserID());
        Query query =  FirebaseDBUtil.getOrdersNodeReference().orderByChild("userID").equalTo(FirebaseDBUtil.getCurrentUserID());
        options = new FirebaseRecyclerOptions.Builder<OrderDB>().setQuery(query, OrderDB.class).build();

        adapter = new YourOrdersAdapter(this, options);

        rvOrderList.setLayoutManager(new LinearLayoutManager(this));
        rvOrderList.setAdapter(adapter);
    }

    private void initializeBackButton() {
        ivBackButton = findViewById(R.id.backIcon);
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}