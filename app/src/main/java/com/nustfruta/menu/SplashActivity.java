package com.nustfruta.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.nustfruta.R;
import com.nustfruta.authentication.LoginPhoneNumberActivity;
import com.nustfruta.models.UserType;
import com.nustfruta.orders.OrderManagementActivity;
import com.nustfruta.orders.OrderTrackingActivity;
import com.nustfruta.orders.YourOrdersActivity;
import com.nustfruta.utility.FirebaseDBUtil;

public class SplashActivity extends AppCompatActivity {

    //TODO: Remove ALL toasts.

    //TODO: either link splash activity to data fetching, or add a custom delay.

    private static final long MIN_SPLASH_DURATION = 1000;
    private long startTime, elapsedTime, remainingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> true);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (FirebaseDBUtil.getCurrentUserID() == null) {
            Intent intent;
            intent = new Intent(this, LoginPhoneNumberActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            // assuming above process was near instant, wait MIN_SPLASH_DURATION before starting next activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            }, MIN_SPLASH_DURATION);

        }
        else {
            // start counting time
            Log.d("Timing", "Starting");
            startTime = System.currentTimeMillis();

            FirebaseDBUtil.getCurrentUserReference().child("userType").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    FirebaseDBUtil.currentUserType = task.getResult().getValue(UserType.class);
                    Intent intent;
                    Log.d("Timing", "Completed");

                    if (FirebaseDBUtil.currentUserType == UserType.ADMIN)
                        intent = new Intent(SplashActivity.this, AdminMenuActivity.class);
                    else
                        intent = new Intent(SplashActivity.this, AdminMenuActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    // onComplete is called when data has been fetched
                    // elapsedTime stores the time taken for this
                    elapsedTime = System.currentTimeMillis() - startTime;
                    // if MIN_SPLASH_DURATION has already passed, instantly start the next activity
                    // otherwise, wait the remaining time till MIN_SPLASH_DURATION passes
                    remainingTime = Math.max(MIN_SPLASH_DURATION - elapsedTime, 0);
                    Log.d("Timing", "Remaining Time: " + remainingTime);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Timing", "Ending");
                            startActivity(intent);
                        }
                    }, remainingTime);
                }
            });
        }
    }
}