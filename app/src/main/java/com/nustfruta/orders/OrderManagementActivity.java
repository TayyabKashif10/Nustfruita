package com.nustfruta.orders;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nustfruta.R;
import com.nustfruta.models.OrderDB;
import com.nustfruta.models.OrderStatus;
import com.nustfruta.models.User;
import com.nustfruta.utility.Constants;
import com.nustfruta.utility.DateFormat;
import com.nustfruta.utility.FirebaseDBUtil;
import com.nustfruta.utility.OrderParser;

import java.util.HashMap;
import java.util.Map;

public class OrderManagementActivity extends AppCompatActivity implements View.OnClickListener {

    Intent intent;

    String orderID;
    OrderDB order;
    String[][] parsedProducts;

    User user;

    final int MAX_ITEMS = 3;
    RecyclerView rvProducts;
    ProductTextAdapter adapter;

    int[] tintColors = new int[6];
    ImageView[] fruits = new ImageView[5];
    ImageView ivBackButton, ivBackArrow, ivForwardArrow;
    TextView tvOrderID, tvOrderDate, tvEstimatedDate, tvTotal, tvName, tvPhone, tvAddress;

    Map<String, Object> statusUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_order_management);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intent = getIntent();

        orderID = intent.getStringExtra("ID");
        statusUpdate = new HashMap<>();
        initializeViews();
        initializeColors();

        FirebaseDBUtil.getOrdersNodeReference().child(orderID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotOrder) {
                order = snapshotOrder.getValue(OrderDB.class);
                parsedProducts = OrderParser.parseProductData(order.getProductData());
                initializeProductsList();

                FirebaseDBUtil.getUsersNodeReference().child(order.getUserID()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshotUser) {
                        user = snapshotUser.getValue(User.class);
                        setViewTexts();
                        updateFruits();
                        updateArrows();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // TODO
                        Log.d("DatabaseCancelled", error.toString());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // TODO
                Log.d("DatabaseCancelled", error.toString());
            }
        });
    }

    private void initializeViews() {
        ivBackButton = findViewById(R.id.backIcon);
        fruits[0] = findViewById(R.id.ivApplePending);
        fruits[1] = findViewById(R.id.ivPearConfirmed);
        fruits[2] = findViewById(R.id.ivCherriesPacking);
        fruits[3] = findViewById(R.id.ivWatermelonOnWay);
        fruits[4] = findViewById(R.id.ivPineappleDelivered);
        tvOrderID = findViewById(R.id.tvOrderID);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvEstimatedDate = findViewById(R.id.tvEstimatedDate);
        tvTotal = findViewById(R.id.tvTotal);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        ivForwardArrow = findViewById(R.id.ivForwardArrow);
        ivBackArrow.setOnClickListener(this);
        ivForwardArrow.setOnClickListener(this);
        ivBackButton.setOnClickListener(this);
    }

    private void initializeColors() {
        tintColors[0] = ContextCompat.getColor(this, R.color.apple_red);
        tintColors[1] = ContextCompat.getColor(this, R.color.pear_green);
        tintColors[2] = ContextCompat.getColor(this, R.color.cherry_red);
        tintColors[3] = ContextCompat.getColor(this, R.color.watermelon_red);
        tintColors[4] = ContextCompat.getColor(this, R.color.pineapple_yellow);
        tintColors[5] = ContextCompat.getColor(this, R.color.black);
    }

    private void initializeProductsList() {
        rvProducts = findViewById(R.id.rvProductList);
        adapter = new ProductTextAdapter(this, parsedProducts);

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);
    }

    private void updateFruits() {
        int status = order.getStatus().ordinal();
        for (int i = 0; i <= status; i++)
            fruits[i].setColorFilter(tintColors[i], PorterDuff.Mode.SRC_ATOP);
        for (int i = fruits.length - 1; i > status; i--)
            fruits[i].setColorFilter(tintColors[5], PorterDuff.Mode.SRC_ATOP);
    }

    private void updateArrows() {
        if (order.getStatus() == OrderStatus.PENDING)
            ivBackArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey));
        else
            ivBackArrow.setColorFilter(ContextCompat.getColor(this, R.color.onBackground));

        if (order.getStatus() == OrderStatus.DELIVERED)
            ivForwardArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey));
        else
            ivForwardArrow.setColorFilter(ContextCompat.getColor(this, R.color.onBackground));

    }

    @Override
    public void onClick(View v) {
        OrderStatus nextStatus;
        if (v.getId() == R.id.ivBackArrow) {
            nextStatus = order.getStatus().move(-1);
            statusUpdate.put("status", nextStatus);

            FirebaseDBUtil.getOrdersNodeReference().child(order.getOrderID()).updateChildren(statusUpdate);
        }
        else if (v.getId() == R.id.ivForwardArrow) {
            nextStatus = order.getStatus().move(1);
            statusUpdate.put("status", nextStatus);
            FirebaseDBUtil.getOrdersNodeReference().child(order.getOrderID()).updateChildren(statusUpdate);
        }
        else if (v.getId() == R.id.backIcon) {
            finish();
        }
    }

    private void setViewTexts() {
        int subtotal = OrderParser.getSubtotal(parsedProducts);

        tvOrderID.setText(order.getOrderID());

        tvOrderDate.setText(order.getDateTime());

        tvEstimatedDate.setText(DateFormat.EEE_DDMMYY(DateFormat.addOneDay(order.getDateTime())));

        tvTotal.setText(String.format("PKR %d", subtotal + Constants.DELIVERY_FEES));

        tvName.setText(user.getFullName());

        tvPhone.setText(user.getFormattedPhone());

        tvAddress.setText(user.getFormattedAddress());
    }

    public void setRowHeight(int height) {
        ViewGroup.LayoutParams rvLayoutParams = rvProducts.getLayoutParams();
        rvLayoutParams.height = height * Math.min(parsedProducts.length, MAX_ITEMS);
        rvProducts.setLayoutParams(rvLayoutParams);
    }
}