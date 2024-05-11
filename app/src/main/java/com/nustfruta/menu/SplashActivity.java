package com.nustfruta.menu;

import android.content.Intent;
import android.os.Bundle;

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
import com.nustfruta.utility.FirebaseDBUtil;

public class SplashActivity extends AppCompatActivity implements SplashCompleteListener{

    //TODO: Remove ALL toasts.

    //TODO: either link splash activity to data fetching, or add a custom delay.

    //TODO: make afzal fix this shit to do the "figure out what kind of user it is during the splash screen"

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

        if (FirebaseDBUtil.getCurrentUserID() == null)
        {
            Intent intent;
            intent = new Intent(this, LoginPhoneNumberActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else {

            FirebaseDBUtil.getCurrentUserReference().child("userType").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                  Intent intent;
                  if (task.getResult().getValue(UserType.class) == UserType.ADMIN)
                  {

                        intent = new Intent(SplashActivity.this, AdminMenuActivity.class);
                  }
                  else
                  {
                      intent = new Intent(SplashActivity.this, MenuActivity.class);
                  }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


        }

    }

}

// stuff to do during splash screen
class Database extends Thread {
    private boolean sleeping;
    final int delay = 1000;
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