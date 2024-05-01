package com.nustfruta.CartAndCheckout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.nustfruta.R;
import com.nustfruta.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements ModifyQuantity {

    int[] productImages = {}; //TODO: INITIALIZE WITH R.drawable.

    public static ArrayList<Product> productArrayList;
    public static CartRecyclerViewAdapter cartRecyclerViewAdapter;

    public Button checkoutButton;

    public String deliveryNotesString;

    public EditText deliveryNotes;

    public static TextView checkoutPrice, checkoutItems, subtotalPrice, totalPrice;

    public static int subtotal = 0;

    public Context context; ;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing product array list
        productArrayList = new ArrayList<>();
        initProductArrayList();

        context = this;


        // If else statement to choose which layout to display depending on size of product array list.
        if (productArrayList.isEmpty()) {
            cartEmpty();
        }
        else
            setContentView(R.layout.cart_activity);

        // Recyclerview initialization
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Creating recyclerview adapter
        cartRecyclerViewAdapter = new CartRecyclerViewAdapter(CartActivity.this);
        recyclerView.setAdapter(cartRecyclerViewAdapter);

        // Turning click animations off on recycler view
        SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        simpleItemAnimator.setSupportsChangeAnimations(false);


        // Configuring the checkout static card display
        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutPrice = findViewById(R.id.checkoutPrice);
        checkoutItems = findViewById(R.id.checkoutItems);
        subtotalPrice = findViewById(R.id.subtotalPrice);
        totalPrice = findViewById(R.id.totalPrice);


        // Calculating subtotal to display when cart activity view is inflated
        for(int i = 0; i < productArrayList.size(); i++)
            subtotal += productArrayList.get(i).getQuantity() * productArrayList.get(i).getUnitPrice();

        // Displaying initial subtotal price, total price, checkout price.
        checkoutPrice.setText("Rs. " + (subtotal + 50));
        subtotalPrice.setText("Rs. " + subtotal);
        totalPrice.setText("Rs. " + (subtotal + 50));
        checkoutItems.setText(String.format("%d items", productArrayList.size()));

    }



    // to avoid memory leak
    public void onDestroy() {
        super.onDestroy();
        cartRecyclerViewAdapter = null;
        checkoutItems = null;
        checkoutPrice = null;
        subtotalPrice = null;
        totalPrice = null;
        CartRecyclerViewAdapter.viewHolder = null;
        CartRecyclerViewAdapter.cartActivity = null;
    }

    private void initProductArrayList() {
        productArrayList.add(new Product(1, 200, "Banana", 2, R.drawable.banana));
        productArrayList.add(new Product(2, 500, "Strawberry", 2, R.drawable.banana));
        productArrayList.add(new Product(3, 300, "Guava", 5, R.drawable.banana));
        productArrayList.add(new Product(4, 100, "Pear", 2, R.drawable.banana));
        productArrayList.add(new Product(5, 50, "Apple", 8, R.drawable.banana));
    }


    public void cartEmpty()
    {
            startActivity(new Intent(CartActivity.this, EmptyBasket.class));
    }


    public void minusButton(int position) {
        // Decrease quantity of given item
        CartActivity.productArrayList.get(position).decrementQuantity();

        // Updating subtotal variable
        CartActivity.subtotal -= CartActivity.productArrayList.get(position).getUnitPrice();

        // If quantity is zero, remove item from product array list and update item count on static card.
        if (CartActivity.productArrayList.get(position).getQuantity() == 0) {

            CartActivity.productArrayList.remove(position);

            CartActivity.checkoutItems.setText(CartActivity.productArrayList.size() + " items");

            CartActivity.cartRecyclerViewAdapter.notifyItemRemoved(position);
        }
        else
            CartActivity.cartRecyclerViewAdapter.notifyItemChanged(position);


        if (CartActivity.productArrayList.isEmpty())
                cartEmpty();


        // Updating the displayed prices on static card view
        CartActivity.checkoutPrice.setText("Rs. " + (CartActivity.subtotal + 50));
        CartActivity.subtotalPrice.setText("Rs. " + CartActivity.subtotal);
        CartActivity.totalPrice.setText("Rs. " + (CartActivity.subtotal + 50));
    }



    public void plusButton(int position) {
        CartActivity.productArrayList.get(position).incrementQuantity();

        // Updating subtotal variable.
        CartActivity.subtotal += CartActivity.productArrayList.get(position).getUnitPrice();

        // Updating the displayed checkout price
        CartActivity.checkoutPrice.setText("Rs. " + (CartActivity.subtotal + 50));
        CartActivity.subtotalPrice.setText("Rs. " + CartActivity.subtotal);
        CartActivity.totalPrice.setText("Rs. " + (CartActivity.subtotal + 50));

        CartActivity.cartRecyclerViewAdapter.notifyItemChanged(position);
    }

}



