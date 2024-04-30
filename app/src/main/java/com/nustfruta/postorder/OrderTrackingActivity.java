package com.nustfruta.postorder;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

public class OrderTrackingActivity extends AppCompatActivity {

    // dummies, provide from database later
    Order order;
    ArrayList<Product> productList;
    int subtotal = 0;
    final int deliveryFees = 99;

    RecyclerView rvProducts;
    OrderTrackingAdapter adapter;

    int[] tintColors = new int[5];
    ImageView[] fruits = new ImageView[5];

    TextView tvOrderStatus, tvOrderID, tvOrderDate, EstimatedDate, tvSubtotal, tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // dummy values, will be provided from database later
        productList = new ArrayList<>();
        productList.add(new Product(1234, 299, "Oranges", 3, 0));
        productList.add(new Product(1314, 199, "Apples", 1,0));
        productList.add(new Product(619, 169, "Bananas", 11,0));
        productList.add(new Product(0, 9999, "sabih", 1,0));
        order = new Order(12345678, Calendar.getInstance(), Calendar.getInstance(),new User(), OrderStatus.ON_WAY, productList);

        tintColors[0] = ContextCompat.getColor(this, R.color.apple_red);
        tintColors[1] = ContextCompat.getColor(this, R.color.pear_green);
        tintColors[2] = ContextCompat.getColor(this, R.color.cherry_red);
        tintColors[3] = ContextCompat.getColor(this, R.color.watermelon_red);
        tintColors[4] = ContextCompat.getColor(this, R.color.pineapple_yellow);

        fruits[0] = findViewById(R.id.ivApplePending);
        fruits[1] = findViewById(R.id.ivPearConfirmed);
        fruits[2] = findViewById(R.id.ivCherriesPacking);
        fruits[3] = findViewById(R.id.ivWatermelonOnWay);
        fruits[4] = findViewById(R.id.ivPineappleDelivered);

        rvProducts = findViewById(R.id.rvProductList);
        // show only first 3 items ordered and ... if any more items
        ArrayList<Product> firstThree = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            if (i + 1 > productList.size())
                break;
            else
                firstThree.add(productList.get(i));

        if (productList.size() > 3)
            firstThree.add(new Product(0, 0, "...", 0,0));

        adapter = new OrderTrackingAdapter(firstThree);
        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(adapter);

        updateFruits();

        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        switch (order.getStatus()) {
            case PENDING:
                tvOrderStatus.setText(R.string.order_status_pending);
                break;
            case CONFIRMED:
                tvOrderStatus.setText(R.string.order_status_confirmed);
                break;
            case PACKING:
                tvOrderStatus.setText(R.string.order_status_packing);
                break;
            case ON_WAY:
                tvOrderStatus.setText(R.string.order_status_on_way);
                break;
            case DELIVERED:
                tvOrderStatus.setText(R.string.order_status_delivered);
                break;
        }

        tvOrderID = findViewById(R.id.tvOrderID);
        tvOrderID.setText(Integer.toString(order.getOrderID()));

        tvOrderDate = findViewById(R.id.tvOrderDate);
        EstimatedDate = findViewById(R.id.tvEstimatedDate);
        tvOrderDate.setText(DateFormat.EEE_DDMMYY(order.getDateTime()));
        EstimatedDate.setText(DateFormat.EEE_DDMMYY(order.getEstDateTime()));

        for (int i = 0; i < productList.size(); i++)
            subtotal += productList.get(i).getUnitPrice() * productList.get(i).getQuantity();
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvSubtotal.setText(Integer.toString(subtotal));

        tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(Integer.toString(subtotal + deliveryFees));
    }

    protected void updateFruits() {
        int status = order.getStatus().ordinal();
        for (int i = 0; i <= status; i++)
            fruits[i].setColorFilter(tintColors[i], PorterDuff.Mode.SRC_ATOP);
    }
}