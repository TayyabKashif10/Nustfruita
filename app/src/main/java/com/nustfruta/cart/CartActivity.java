package com.nustfruta.cart;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.nustfruta.R;
import com.nustfruta.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements ModifyQuantity {

    int[] productImages = {}; //TODO: INITIALIZE WITH R.drawable.

    public ArrayList<Product> productArrayList;
    public  CartRecyclerViewAdapter cartRecyclerViewAdapter;

    public Button checkoutButton;

    public String deliveryNotesString;

    public EditText deliveryNotes;

    public TextView checkoutPrice;

    public int subtotal = 0;

    public Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing product array list
        productArrayList = new ArrayList<>();
        initProductArrayList();

        context = this;

        // If else statement to choose which layout to display depending on size of product array list.
        if (productArrayList.isEmpty()) {
            setContentView(R.layout.empty_basket);
        }
        else
        {
            setContentView(R.layout.cart_activity);
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
            checkoutPrice.setText("PKR " + (subtotal + 50));


            checkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Place order, send order to database
                }
            });
        }
    }

    // to avoid memory leak

    private void initProductArrayList() {
        productArrayList.add(new Product(1, 200, "Banana", 2, R.drawable.banana));
        productArrayList.add(new Product(2, 500, "Strawberry", 2, R.drawable.banana));
        productArrayList.add(new Product(3, 300, "Guava", 5, R.drawable.banana));
        productArrayList.add(new Product(4, 100, "Pear", 2, R.drawable.banana));
        productArrayList.add(new Product(5, 50, "Apple", 2, R.drawable.banana));
    }


    public void cartEmpty()
    {
            setContentView(R.layout.empty_basket);
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
                cartEmpty();


        // Updating the displayed prices on static card view
        checkoutPrice.setText("PKR " + (subtotal + 50));

        cartRecyclerViewAdapter.notifyItemChanged(productArrayList.size());
    }



    public void plusButton(CartRecyclerViewAdapter.ViewHolder holder, int position) {
        productArrayList.get(position).incrementQuantity();

        // Updating subtotal variable.
        subtotal += productArrayList.get(position).getUnitPrice();

        // Updating the displayed checkout price
        checkoutPrice.setText("PKR " + (subtotal + 50));

        cartRecyclerViewAdapter.notifyItemChanged(productArrayList.size());
        cartRecyclerViewAdapter.notifyItemChanged(position);
    }

}



