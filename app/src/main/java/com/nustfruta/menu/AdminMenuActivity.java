package com.nustfruta.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.nustfruta.R;
import com.nustfruta.authentication.LoginPhoneNumberActivity;
import com.nustfruta.menu_fragments.MenuFragmentAdapter;
import com.nustfruta.misc.AboutUsActivity;
import com.nustfruta.orders.OrderManagementActivity;
import com.nustfruta.productAdding.AddProductActivity;
import com.nustfruta.utility.Constants;

public class AdminMenuActivity extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout drawerLayout;

    // drawer children
    LinearLayout manageOrders, about, logout;

    TabLayout tabLayout;

    ViewPager2 viewPager;

    MenuFragmentAdapter fragmentAdapter;

    ImageView optionsIcon;

    Button addProductBtn;

    @Override
    public void onClick(View v) {
        if (v.getId() == optionsIcon.getId())
        {
            openDrawer();
        }
        else if (v.getId() == manageOrders.getId()) {
            navigateOut(OrderManagementActivity.class);
        } else if (v.getId() == about.getId()) {
            navigateOut(AboutUsActivity.class);
        } else if (v.getId() == logout.getId()) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {

                // clear backstack and send user to sign in page.
                Intent intent = new Intent(AdminMenuActivity.this, LoginPhoneNumberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            });
        } else if (v.getId() == addProductBtn.getId()) {
            navigateOut(AddProductActivity.class);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_menu);

        initializeViews();
        attachListeners();
        setupMenuFragments();

    }

    @Override
    protected void onResume() {
        closeDrawer();
        super.onResume();
    }


    public void openDrawer()
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer()
    {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    // navigate to other fragments from the drawer.
    public void navigateOut(Class activity)
    {
        Intent intent = new Intent(AdminMenuActivity.this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void initializeViews()
    {

        optionsIcon = findViewById(R.id.hamburgerIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        manageOrders=findViewById(R.id.manageOrdersRow);
        about=findViewById(R.id.aboutRow); logout=findViewById(R.id.logoutRow);
        logout = findViewById(R.id.logoutRow);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        addProductBtn = findViewById(R.id.addProductButton);
    }

    public void attachListeners()
    {
        optionsIcon.setOnClickListener(this);
        manageOrders.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);
        addProductBtn.setOnClickListener(this);
    }

    public void setupMenuFragments()
    {
        fragmentAdapter = new MenuFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(fragmentAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position)->{

            switch (position)
            {
                case 0:
                    //fruit tab
                    tab.setIcon(R.drawable.fruit_apple);
                    break;
                case 1:
                    // salad tab
                    tab.setIcon(R.drawable.fruit_bowl);
                    break;
                case 2:
                    // bundle tab
                    tab.setIcon(R.drawable.fruit_bundle);
            }

        }).attach();

    }
}