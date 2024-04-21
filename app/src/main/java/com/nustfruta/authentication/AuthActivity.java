package com.nustfruta.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nustfruta.R;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.login)
        {
            //TODO: implement login logic
        }
        else if (v.getId() == R.id.register)
        {
            //TODO: implement register logic
        } else
        {
            assert v.getId() == R.id.guestText;

            //TODO: implement guest account logic
        }
    }

    Button login, register;
    TextView guestText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.auth_activity);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.authScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
    }

    public void initializeViews()
    {
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        guestText = findViewById(R.id.guestText);
    }
}
