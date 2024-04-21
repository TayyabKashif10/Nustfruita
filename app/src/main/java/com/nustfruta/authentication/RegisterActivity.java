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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {

    }

    Button login, register;
    TextView guestText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_activity);

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
