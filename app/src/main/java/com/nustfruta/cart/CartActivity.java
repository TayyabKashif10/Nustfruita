package com.nustfruta.cart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.firebase.database.DatabaseReference;
import com.nustfruta.R;
import com.nustfruta.authentication.LoginPhoneNumberActivity;
import com.nustfruta.dialog.DialogFactory;
import com.nustfruta.dialog.LoginDialogEventListener;
import com.nustfruta.menu.MenuActivity;
import com.nustfruta.models.CartProduct;
import com.nustfruta.models.OrderDB;
import com.nustfruta.models.OrderStatus;
import com.nustfruta.models.UserType;
import com.nustfruta.orders.OrderTrackingActivity;
import com.nustfruta.utility.Constants;
import com.nustfruta.utility.FirebaseDBUtil;
import com.nustfruta.utility.OrderParser;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements CartCardButtonListener {
    public ArrayList<CartProduct> productArrayList;
    public  CartRecyclerViewAdapter cartRecyclerViewAdapter;

    public Button backToMenuButton;

    public CardView checkoutButton;

    public TextView checkoutPrice;

    public int subtotal = 0;

    public Context context;

    ImageView ivBackButton;

    Intent backIntent = new Intent();

    LoginDialogEventListener loginDialogEventListener = new LoginDialogEventListener() {
        @Override
        public void onGoBackClicked() {
            DialogFactory.destroyLoginDialog();
        }

        @Override
        public void onLoginClicked() {
            DialogFactory.destroyLoginDialog();
            Intent intent = new Intent(CartActivity.this, LoginPhoneNumberActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing product array list
        initProductArrayList();

        // register the result so that we can return the arrayList to menu.
        registerResult();

        context = this;

        // If else statement to choose which layout to display depending on size of product array list.
        if (productArrayList.isEmpty()) {
            initializeWithEmptyCart();
        }
        else
        {
            EdgeToEdge.enable(this);
            setContentView(R.layout.cart_activity);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cartActivityLayout), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Recyclerview initialization
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Creating recyclerview adapter
            cartRecyclerViewAdapter = new CartRecyclerViewAdapter(this, productArrayList);
            recyclerView.setAdapter(cartRecyclerViewAdapter);

            // Turning click animations off on recycler view
            SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) recyclerView.getItemAnimator();
            simpleItemAnimator.setSupportsChangeAnimations(false);


            // Configuring the checkout static card display
            checkoutButton = findViewById(R.id.checkoutButton);
            checkoutPrice = findViewById(R.id.checkoutPrice);



            // Calculating subtotal to display when cart activity view is inflated
            for(int i = 0; i < productArrayList.size(); i++)
                subtotal += productArrayList.get(i).getQuantity() * productArrayList.get(i).getUnitPrice();

            // Displaying initial subtotal price, total price, checkout price.
            checkoutPrice.setText("PKR " + (subtotal + Constants.DELIVERY_FEES));


            checkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (FirebaseDBUtil.currentUserType == UserType.GUEST)
                    {
                        DialogFactory.createLoginDialog(CartActivity.this, true, loginDialogEventListener);
                        return;
                    }
                    checkout();
                }
            });

            initializeBackButton();
        }
    }


    private void initProductArrayList() {
        productArrayList = (ArrayList<CartProduct>)getIntent().getExtras().get("productArray");
    }


    public void initializeWithEmptyCart()
    {
        setContentView(R.layout.empty_basket);
        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.emptyBasketLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backToMenuButton = findViewById(R.id.backToMenuButton);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    public void minusButton(CartRecyclerViewAdapter.ViewHolder holder, int position) {

        // Decrease quantity of given item
        productArrayList.get(position).decrementQuantity();

        // Updating subtotal variable
        subtotal -= productArrayList.get(position).getUnitPrice();

        // If quantity is zero, remove item from product array list and update item count on static card.
        if (productArrayList.get(position).getQuantity() == 0) {

            productArrayList.remove(position);

            cartRecyclerViewAdapter.notifyItemRemoved(position);
        }
        else
            cartRecyclerViewAdapter.notifyItemChanged(position);


        if (productArrayList.isEmpty())
                initializeWithEmptyCart();


        // Updating the displayed prices on static card view
        checkoutPrice.setText("PKR " + (subtotal + Constants.DELIVERY_FEES));

        cartRecyclerViewAdapter.notifyItemChanged(productArrayList.size());

        registerResult();
    }



    public void plusButton(CartRecyclerViewAdapter.ViewHolder holder, int position) {
        productArrayList.get(position).incrementQuantity();

        // Updating subtotal variable.
        subtotal += productArrayList.get(position).getUnitPrice();

        // Updating the displayed checkout price
        checkoutPrice.setText("PKR " + (subtotal + Constants.DELIVERY_FEES));

        cartRecyclerViewAdapter.notifyItemChanged(productArrayList.size());
        cartRecyclerViewAdapter.notifyItemChanged(position);

        registerResult();
    }

    public void registerResult()
    {
        backIntent.putExtra("productArray", productArrayList);
        setResult(Constants.CART_RESULT_CODE, backIntent);
    }

    public void checkout()
    {
        String orderID = FirebaseDBUtil.getOrdersNodeReference().push().getKey();
        DatabaseReference orderReference = FirebaseDBUtil.getOrdersNodeReference().child(orderID);
        OrderDB currentOrder = new OrderDB();
        currentOrder.setProductData(OrderParser.getProductDataString(productArrayList));
        currentOrder.setOrderID(orderID);
        currentOrder.setStatus(OrderStatus.PENDING);
        orderReference.setValue(currentOrder);

        // store ID in user's orders node.
        FirebaseDBUtil.getCurrentUserReference().child("orders").push().setValue(orderID);

        productArrayList.clear();

        // navigate to order tracking
        Intent intent = new Intent(this, OrderTrackingActivity.class);
        intent.putExtra("ID", orderID);
        finish();  // prevent user from navigating back to checkout

        startActivity(intent);
    }
}

