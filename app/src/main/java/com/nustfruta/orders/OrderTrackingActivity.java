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
    import com.nustfruta.utility.Constants;
    import com.nustfruta.utility.DateFormat;
    import com.nustfruta.utility.FirebaseDBUtil;
    import com.nustfruta.utility.OrderParser;

    public class OrderTrackingActivity extends AppCompatActivity {

        Intent intent;
        String orderID;
        OrderDB order;
        String[][] parsedProducts;
        final int MAX_ITEMS = 3;
        RecyclerView rvProducts;
        ProductTextAdapter adapter;
        int[] tintColors = new int[5];
        ImageView[] fruits = new ImageView[5];
        ImageView ivBackButton;
        TextView tvOrderStatus, tvOrderID, tvOrderDate, tvEstimatedDate, tvSubtotal, tvTotal;

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

            intent = getIntent();

            orderID = intent.getStringExtra("ID");
            FirebaseDBUtil.getOrdersNodeReference().child(orderID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    order = snapshot.getValue(OrderDB.class);
                    parsedProducts = OrderParser.parseProductData(order.getProductData());
                    updateFruits();
                    setViewTexts();
                    initializeProductsList();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("DatabaseCancelled", error.toString());
                }
            });

            initializeViews();
            initializeColors();
            initializeBackButton();
        }

        private void initializeViews() {
            ivBackButton = findViewById(R.id.backIcon);
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
            adapter = new ProductTextAdapter(this, parsedProducts);

            rvProducts.setLayoutManager(new LinearLayoutManager(this));
            rvProducts.setAdapter(adapter);
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

        //TODO: afzal fix this.
        private void updateFruits() {
            int status = order.getStatus().ordinal();
            for (int i = 0; i <= status; i++)
                fruits[i].setColorFilter(tintColors[i], PorterDuff.Mode.SRC_ATOP);
            for (int i = fruits.length - 1; i > status; i--)
                fruits[i].setColorFilter(tintColors[5], PorterDuff.Mode.SRC_ATOP);
            tvOrderStatus.setTextColor(tintColors[status]);
        }

        private void setViewTexts() {
            int subtotal = OrderParser.getSubtotal(parsedProducts);

            tvOrderStatus.setText(getOrderStatus());

            tvOrderID.setText(order.getOrderID());

            tvOrderDate.setText(order.getDateTime());

            tvEstimatedDate.setText(DateFormat.EEE_DDMMYY(DateFormat.addOneDay(order.getDateTime())));

            tvSubtotal.setText(String.format("PKR %d", subtotal));
            tvTotal.setText(String.format("PKR %d", subtotal + Constants.DELIVERY_FEES));
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

        public void setRowHeight(int height) {
            ViewGroup.LayoutParams rvLayoutParams = rvProducts.getLayoutParams();
            rvLayoutParams.height = height * Math.min(parsedProducts.length, MAX_ITEMS);
            rvProducts.setLayoutParams(rvLayoutParams);
        }
    }