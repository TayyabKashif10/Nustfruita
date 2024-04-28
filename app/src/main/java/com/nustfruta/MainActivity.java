package com.nustfruta;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.FirebaseDatabase;
import com.nustfruta.authentication.LoginOTPActivity;
import com.nustfruta.authentication.LoginPhoneNumberActivity;
import com.nustfruta.postorder.OrderTracking;
import com.nustfruta.utility.FirebaseUtil;

public class MainActivity extends AppCompatActivity {

    //TODO: Remove ALL toasts.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        //TODO: Add a splash activity for logo, which moves on to loginPhoneNumberActivity as a final result.

        if (FirebaseUtil.getCurrentUserID() == null)
        {
            Intent authenticate = new Intent(this, OrderTracking.class);
            startActivity(authenticate);
        }
        else
        {

        }

    }
}