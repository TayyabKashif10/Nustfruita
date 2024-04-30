package com.nustfruta;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nustfruta.authentication.LoginPhoneNumberActivity;
import com.nustfruta.authentication.ProfileActivity;
import com.nustfruta.utility.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    //TODO: Remove ALL toasts.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //TODO: Develop the splash activity xml

        if (FirebaseUtil.getCurrentUserID() == null)
        {
            Intent authenticate = new Intent(this, LoginPhoneNumberActivity.class);
            startActivity(authenticate);
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }
}