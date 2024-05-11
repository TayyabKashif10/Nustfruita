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

import com.nustfruta.R;
import com.nustfruta.models.Order;
import com.nustfruta.models.OrderStatus;
import com.nustfruta.models.LegacyProduct;
import com.nustfruta.models.User;

import java.util.ArrayList;
import java.util.Calendar;

public class YourOrdersActivity extends AppCompatActivity {

    // dummy, provide from database later
    ArrayList<Order> orderList;

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

        orderList = new ArrayList<>();

        ArrayList<LegacyProduct> productList = new ArrayList<>();
        productList.add(new LegacyProduct(1234, 299, "Oranges", 3, 0));
        productList.add(new LegacyProduct(1314, 199, "Apples", 1,0));
        orderList.add(new Order("firstOrder", Calendar.getInstance(), Calendar.getInstance(), new User(), OrderStatus.ON_WAY, productList));

        productList.clear();
        productList.add(new LegacyProduct(619, 169, "Bananas", 11,0));
        productList.add(new LegacyProduct(0, 9999, "sabih", 1,0));
        orderList.add(new Order("secondOrder", Calendar.getInstance(), Calendar.getInstance(), new User(), OrderStatus.DELIVERED, productList));

        initializeOrderList();
    }

    private void initializeOrderList() {
        rvOrderList = findViewById(R.id.rvOrderList);
        adapter = new YourOrdersAdapter(this, orderList);

        rvOrderList.setLayoutManager(new LinearLayoutManager(this));
        rvOrderList.setAdapter(adapter);
    }


    public void expandCard(String orderID)
    {
        Intent intent = new Intent(this, OrderTrackingActivity.class);
        intent.putExtra("ID", orderID);
        startActivity(intent);

    }
}