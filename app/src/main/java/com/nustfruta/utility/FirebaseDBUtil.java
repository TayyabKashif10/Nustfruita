package com.nustfruta.utility;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nustfruta.models.ProductDB;
import com.nustfruta.models.User;
import com.nustfruta.models.UserType;

abstract public class FirebaseDBUtil {

    // this is loaded at the start of the activity, and can never change, so it is loaded once and then persists throughout
    // the app.
    public static UserType currentUserType;

    public static FirebaseDatabase database;

    public static User currentUserObject;

    public static String getCurrentUserID()
    {
        /*this is NUll if there is no signed in user.
         once a user signs in the App with mAuth.signWithCredentials(), FirebaseAuth stores that user details
         somewhere in app memory, so this method will return currentUserID() as long as we dont clear data after signing in*/
        return FirebaseAuth.getInstance().getUid();
    }

    public static DatabaseReference getCurrentUserReference()
    {
        return database.getReference("users").child(getCurrentUserID());
    }

    public static DatabaseReference getFruitFactNodeReference()
    {
        return database.getReference("fruit_facts");
    }

    public static DatabaseReference getOrdersNodeReference()
    {
        return database.getReference("orders");
    }

    public static DatabaseReference getProductsNodeRerefence()
    {
        return database.getReference("products");
    }

    // set the callBack listeners for the current user to keep it updated.
    //  by setting this up, the current user persists throughout the app, through google auth itself
    static
    {

        database = FirebaseDatabase.getInstance();
        fetchCurrentUser();
    }

    // syncs the instance of the currentUser object with the realtime database
    public static void fetchCurrentUser()
    {
        if (getCurrentUserID()==null)
        {
            return;
        }
        getCurrentUserReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currentUserObject = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // Store user inside database, with the key as its UID.
    public static void storeUser(User user, String UID)
    {
        database.getReference("users").child(UID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                fetchCurrentUser();
            }
        });
    }


    public static void storeProductDB (ProductDB product, String category) {

        if (category.equals("Fruit"))
            database.getReference("products/fruits").child(product.getProductName()).setValue(product);

        else
            database.getReference("products/salads").child(product.getProductName()).setValue(product);
    }

}
