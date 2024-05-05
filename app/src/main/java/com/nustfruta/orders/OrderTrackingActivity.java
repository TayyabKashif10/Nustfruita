package com.nustfruta.orders;

import static com.nustfruta.utility.Constants.DELIVERY_FEES;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nustfruta.R;
import com.nustfruta.models.Order;
import com.nustfruta.models.OrderStatus;
import com.nustfruta.models.Product;
import com.nustfruta.models.User;
import com.nustfruta.utility.DateFormat;

import java.util.Calendar;
import java.util.ArrayList;

public class OrderTrackingActivity extends AppCompatActivity implements HeightListener {

    Intent intent;

    // dummies, provide from Your Orders activity later
    Order order;
    ArrayList<Product> productList;

    final int maxItems = 3;
    RecyclerView rvProducts;
    OrderTrackingAdapter adapter;

    int[] tintColors = new int[5];
    ImageView[] fruits = new ImageView[5];
    TextView tvOrderStatus, tvOrderID, tvOrderDate, tvEstimatedDate, tvSubtotal, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intent = getIntent();

        // dummy values, will be provided from database later
        productList = new ArrayList<>();
        productList.add(new Product(1234, 299, "Oranges", 3, 0));
        productList.add(new Product(1314, 199, "Apples", 1,0));
        productList.add(new Product(619, 169, "Bananas", 11,0));
        productList.add(new Product(0, 9999, "sabih", 1,0));
        order = new Order("12345678", Calendar.getInstance(), Calendar.getInstance(),new User(), OrderStatus.ON_WAY, productList);

        initializeViews();
        initializeColors();
        initializeProductsList();

        updateFruits();
        setViewTexts();

        Toast.makeText(this, intent.getStringExtra("ID"), Toast.LENGTH_SHORT).show();
    }

    private void initializeViews() {
        fruits[0] = findViewById(R.id.ivApplePending);
        fruits[1] = findViewById(R.id.ivPearConfirmed);
        fruits[2] = findViewById(R.id.ivCherriesPacking);
        fruits[3] = findViewById(R.id.ivWatermelonOnWay);
        fruits[4] = findViewById(R.id.ivPineappleDelivered);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderID = findViewById(R.id.tvOrderID);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvEstimatedDate = findViewById(R.id.tvEstimatedDate);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);
    }

    private void initializeColors() {
        tintColors[0] = ContextCompat.getColor(this, R.color.apple_red);
        tintColors[1] = ContextCompat.getColor(this, R.color.pear_green);
        tintColors[2] = ContextCompat.getColor(this, R.color.cherry_red);
        tintColors[3] = ContextCompat.getColor(this, R.color.watermelon_red);
        tintColors[4] = ContextCompat.getColor(this, R.color.pineapple_yellow);
    }

    private void initializeProductsList() {
        rvProducts = findViewById(R.id.rvProductList);
        adapter = new OrderTrackingAdapter(productList);

        adapter.setHeightListener(this);

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);
    }

    private void updateFruits() {
        int status = order.getStatus().ordinal();
        for (int i = 0; i <= status; i++)
            fruits[i].setColorFilter(tintColors[i], PorterDuff.Mode.SRC_ATOP);
        tvOrderStatus.setTextColor(tintColors[status]);
    }

    private void setViewTexts() {
        tvOrderStatus.setText(getOrderStatus());

        tvOrderID.setText(order.getOrderID());

        tvOrderDate.setText(DateFormat.EEE_DDMMYY(order.getDateTime()));
        tvEstimatedDate.setText(DateFormat.EEE_DDMMYY(order.getEstDateTime()));

        tvSubtotal.setText(String.format("PKR %d", order.getTotal() - DELIVERY_FEES));
        tvTotal.setText(String.format("PKR %d", order.getTotal()));
    }

    private int getOrderStatus() {
        switch (order.getStatus()) {
            case PENDING:
                return R.string.order_status_pending;
            case CONFIRMED:
                return R.string.order_status_confirmed;
            case PACKING:
                return R.string.order_status_packing;
            case ON_WAY:
                return R.string.order_status_on_way;
            case DELIVERED:
                return R.string.order_status_delivered;
            default:
                return R.string.error;
        }
    }

    @Override
    public void onHeightObtained(int height) {
        ViewGroup.LayoutParams rvLayoutParams = rvProducts.getLayoutParams();
        rvLayoutParams.height = height * Math.min(productList.size(), maxItems);
        rvProducts.setLayoutParams(rvLayoutParams);
    }
}