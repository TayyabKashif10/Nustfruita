package com.nustfruta.utility;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nustfruta.MainActivity;
import com.nustfruta.models.User;

public class FirebaseUtil {

    public static FirebaseDatabase database;

    // this is made a class field so that it can be updated by the listener throughout the app runtime.
    public static User currentUserObject;
    public static String getCurrentUserID()
    {

//         this is NUll if there is no signed in user.
//         once a user signs in the App with mAuth.signWithCredentials(), FirebaseAuth stores that user details
//         somewhere in app memory, so this method will return currentUserID() as long as we dont clear data after signing in
        return FirebaseAuth.getInstance().getUid();
    }

    // set the callBack listeners for the current user to keep it updated.
    //  by setting this up, the current user persists throughout the app, through google auth itself
    static
    {

        database = FirebaseDatabase.getInstance();

    }

    public User getCurrentUser()
    {
        if (getCurrentUserID()==null)
        {
            return null;
        }
        FirebaseDatabase.getInstance().getReference("users").child(getCurrentUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currentUserObject = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return currentUserObject;
    }

    // Store user inside database, with the key as its UID.
    public static void storeUser(User user, String UID)
    {
        database.getReference("users").child(UID).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                System.out.println("Added successfully");
            }
        });
    }


}
