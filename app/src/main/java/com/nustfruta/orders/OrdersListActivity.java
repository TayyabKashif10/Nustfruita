package com.nustfruta.orders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.nustfruta.R;
import com.nustfruta.orders_fragments.OrdersFragmentAdapter;

public class OrdersListActivity extends AppCompatActivity {

    ImageView ivBackButton;

    ViewPager2 viewPager;
    TabLayout tabLayout;
    OrdersFragmentAdapter adapter;

    TextView tvActivityLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_orders_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupFragments();
        initializeBackButton();
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tvActivityLabel = findViewById(R.id.tvActivityLabel);
        tvActivityLabel.setText(R.string.all_orders);
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

    public void setupFragments()
    {
        adapter = new OrdersFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position)->{

            switch (position)
            {
                case 0:
                    // active tab
                    tab.setIcon(R.drawable.box_open);
                    break;
                case 1:
                    // delivered tab
                    tab.setIcon(R.drawable.box_closed_tick);
                    break;
            }

        }).attach();

    }

    public void expandCard(String orderID)
    {
        Intent intent = new Intent(this, OrderManagementActivity.class);
        intent.putExtra("ID", orderID);
        startActivity(intent);
    }
}