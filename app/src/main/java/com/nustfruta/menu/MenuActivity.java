package com.nustfruta.menu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nustfruta.R;
import com.nustfruta.authentication.LoginPhoneNumberActivity;
import com.nustfruta.authentication.ProfileActivity;
import com.nustfruta.cart.CartActivity;
import com.nustfruta.menu_fragments.MenuFragmentAdapter;
import com.nustfruta.models.CartProduct;
import com.nustfruta.models.ProductDB;
import com.nustfruta.orders.YourOrdersActivity;
import com.nustfruta.utility.Constants;
import com.nustfruta.utility.FirebaseDBUtil;
import com.nustfruta.utility.FirebaseStorageUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{

    ProductArrayViewModel productArrayViewModel;

    ActivityResultLauncher<Intent> cartLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Constants.CART_RESULT_CODE)
                    {
                        Intent intent = result.getData();
                        if (intent != null)
                        {
                            ArrayList<CartProduct> updatedArray = (ArrayList<CartProduct>) intent.getExtras().get("productArray");
                            if (updatedArray!=null)
                            {
                                // update view model, this will also trigger the observer which automatically updates the counter.
                                productArrayViewModel.updateArray(updatedArray);
                            }

                        }
                    }

                }
            }

    );

    TextView productCounter;
    @Override
    public void onClick(View v) {

        if (v.getId()==optionsIcon.getId())
        {
            openDrawer();
        } else if (v.getId()==profile.getId()) {
            navigateOut(ProfileActivity.class);
        }
        else if (v.getId()== basketButton.getId())
        {
            Intent intent = new Intent(this, CartActivity.class);
            intent.putExtra("productArray", productArrayViewModel.getArray());
            cartLauncher.launch(intent);

        } else if (v.getId() == logout.getId()) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(task -> {

                    // clear backstack and send user to sign in page.
                    Intent intent = new Intent(MenuActivity.this, LoginPhoneNumberActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
            });
        }

        else if (v.getId() == orders.getId()) {
            navigateOut(YourOrdersActivity.class);
        }

        //TODO: navigate out for other buttons.
    }

    DrawerLayout drawerLayout;

    ImageButton basketButton;
    LinearLayout orders, profile, about, logout;

    TabLayout tabLayout;

    ViewPager2 viewPager;
    MenuFragmentAdapter fragmentAdapter;

    ImageView optionsIcon;

    // TODO: remove this, should always be enabled
    boolean factChangeEnabled = false;

    Handler factChangeHandler;

    // the runnable is saved to be able to remove it in onPause.
    Runnable factChangeRunnable;
    Random random = new Random();

    CardView cardA, cardB, currentCard;
    TextView textA, textB;

    // number of fruit facts stored in database
    int fruitFactNumber;

    String fetchedFact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        fetchedFact = getString(R.string.sample_fact);
        initializeViews();
        attachListeners();
        setUpProductArrayModel();
        setupMenuFragments();
        syncFruitFactNumber();
        factChangeHandler = new Handler();

    }
    @Override
    protected void onResume() {
        fragmentAdapter.notifyDataSetChanged();

        /*
        * the drawer is left open when user navigates to another activity (this activity is set to pause) so if its open when user comes back, close it.
        * i found this to be a better placement then putting it in pause, because in pause the system first waits for the drawer to close, which is jittery
        * in on resume its not jittery at all.
        * */
        closeDrawer();
        super.onResume();
        factChangeHandler.postDelayed( factChangeRunnable = new Runnable() {
            public void run() {
                changeFact();
                factChangeHandler.postDelayed(factChangeRunnable, Constants.FACT_CHANGE_DELAY);
            }
        }, Constants.FACT_CHANGE_DELAY);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // stop handler when activity is paused.
        factChangeHandler.removeCallbacks(factChangeRunnable);

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
        Intent intent = new Intent(MenuActivity.this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    public void attachListeners()
    {
        optionsIcon.setOnClickListener(this);
        orders.setOnClickListener(this);
        profile.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);
        basketButton.setOnClickListener(this);
    }

    public void setUpProductArrayModel()
    {
        productArrayViewModel = new ViewModelProvider(this).get(ProductArrayViewModel.class);
        productArrayViewModel.initializeArray();
        productArrayViewModel.getLiveArray().observe(this, item->{

        // update product Counter
        productCounter.setText(String.valueOf(item.size()));

        });
    }

    public void syncFruitFactNumber()
    {
        FirebaseDBUtil.getFruitFactNodeReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                fruitFactNumber = (int) snapshot.getChildrenCount();
                setFruitFact(textA);
                setFruitFact(textB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("dbError",error.getMessage());
            }
        });
    }

    public void initializeViews()
    {
        optionsIcon = findViewById(R.id.hamburgerIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        cardA = findViewById(R.id.cardA);
        currentCard = cardA;
        cardB = findViewById(R.id.cardB);
        textA = findViewById(R.id.textA);
        textB = findViewById(R.id.textB);
        orders=findViewById(R.id.orderRow); profile=findViewById(R.id.profileRow);
        about=findViewById(R.id.aboutRow); logout=findViewById(R.id.logoutRow);
        logout = findViewById(R.id.logoutRow);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        productCounter = findViewById(R.id.productCounter);
        basketButton = findViewById(R.id.basketButton);
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

    public void setFruitFact(TextView v)
    {
        if (fruitFactNumber == 0)
        {
            return;
        }

        int randomPos = random.nextInt(fruitFactNumber);
        FirebaseDBUtil.getFruitFactNodeReference().child(String.valueOf(randomPos)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                v.setText(task.getResult().getValue(String.class));
            }
        });
    }

    public void changeFact() {

        if (!factChangeEnabled)
        {
            return;
        }

        if (currentCard.getId() == cardA.getId())
        {
            animateCardOut(cardA);
            animateCardIn(cardB);

            currentCard = cardB;
        }
        else
        {
            animateCardOut(cardB);
            animateCardIn(cardA);

            currentCard = cardA;
        }
    }

    private void animateCardOut(final View view) {
        ViewPropertyAnimator animator = view.animate()
                .translationX(view.getWidth())
                .alpha(0)
                .setDuration(500);

        animator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);

                // change text of the card that becomes out of view.
                if (view.getId() == cardA.getId())
                {
                    setFruitFact(textA);
                }
                else
                {
                    setFruitFact(textB);
                }
            }
        });
    }

    private void animateCardIn(final View view) {
        view.setTranslationX(-view.getWidth());
        view.setVisibility(View.VISIBLE);

        view.animate()
                .translationX(0)
                .alpha(1)
                .setDuration(500)
                .setListener(null);
    }


    public void displayBottomSheet(CartProduct product)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);

        ImageView productImage = dialog.findViewById(R.id.bottomSheetProductImage);
        ImageButton plusButton = dialog.findViewById(R.id.bottomSheetPlusButton);
        ImageButton minusButton = dialog.findViewById(R.id.bottomSheetMinusButton);
        CardView addCard = dialog.findViewById(R.id.addCard);
        TextView productPrice = addCard.findViewById(R.id.bottomSheetProductPrice);
        TextView quantity = dialog.findViewById(R.id.bottomSheetQuantity);
        TextView productName = dialog.findViewById(R.id.bottomSheetProductName);
        TextView unitPrice = dialog.findViewById(R.id.bottomSheetUnitPrice);
        TextView productUnit = dialog.findViewById(R.id.bottomSheetProductUnit);
        FirebaseStorageUtil.BindImage(productImage, product.getImageURL());

        productName.setText(product.getProductName());
        quantity.setText(String.valueOf(product.getQuantity()));
        productPrice.setText(String.valueOf(product.getQuantity()*product.getUnitPrice()));
        unitPrice.setText("Rs " + product.getUnitPrice());
        productUnit.setText(product.getProductUnit());

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setQuantity(product.getQuantity() + 1);
                quantity.setText(String.valueOf(product.getQuantity()));
                productPrice.setText(String.valueOf(product.getQuantity()*product.getUnitPrice()));
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getQuantity() == 1)
                {
                    dialog.dismiss();
                }
                else
                {
                    product.setQuantity(product.getQuantity() - 1);
                    quantity.setText(String.valueOf(product.getQuantity()));
                    productPrice.setText(String.valueOf(product.getQuantity()*product.getUnitPrice()));
                }
            }
        });

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content),"Added to Cart.",Snackbar.LENGTH_SHORT).setBackgroundTint(Constants.COLOR_PRIMARY).show();
                productArrayViewModel.addProduct(product);
                dialog.dismiss();
            }
        });

        // display the dialog/bottom sheet

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.BottomSheetAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

}