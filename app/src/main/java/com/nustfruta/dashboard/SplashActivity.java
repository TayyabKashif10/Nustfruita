package com.nustfruta.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nustfruta.R;
import com.nustfruta.authentication.LoginPhoneNumberActivity;
import com.nustfruta.utility.FirebaseUtil;

public class SplashActivity extends AppCompatActivity implements SplashCompleteListener{

    //TODO: Remove ALL toasts.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database test = new Database();
        test.setSplashCompleteListener(this);
        test.start();

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(test::isSleeping);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    // switches to next activity once splash screen is closed
    @Override
    public void onSplashComplete() {
        if (FirebaseUtil.getCurrentUserID() == null)
        {
            Intent authenticate = new Intent(this, LoginPhoneNumberActivity.class);
            startActivity(authenticate);
        }
        else {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
    }
}

// stuff to do during splash screen
class Database extends Thread {
    private boolean sleeping;
    final int delay = 3000;
    private SplashCompleteListener splashCompleteListener;

    public Database() {}

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSplashCompleteListener(SplashCompleteListener splashCompleteListener) {
        this.splashCompleteListener = splashCompleteListener;
    }

    public void run() {
        sleeping = true;
        try {
            Thread.sleep(delay);
        }
        catch (InterruptedException e) {    }
        splashCompleteListener.onSplashComplete();
        sleeping = false;
    }
}