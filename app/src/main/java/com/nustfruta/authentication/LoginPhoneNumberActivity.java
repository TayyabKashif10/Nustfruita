package com.nustfruta.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;
import com.nustfruta.R;

public class LoginPhoneNumberActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onClick(View v) {

        if (v.getId() == sendOTPBtn.getId())
        {
            if (!countryCodePicker.isValidFullNumber())
            {
                carrierPhoneNumberField.setError("Invalid Phone Number");
            }
            else
            {
                Intent intent = new Intent(LoginPhoneNumberActivity.this, LoginOTPActivity.class);
                intent.putExtra("phoneNumber", countryCodePicker.getFullNumberWithPlus());
                startActivity(intent);
            }
        }
    }

    Button sendOTPBtn;
    CountryCodePicker countryCodePicker;

    EditText carrierPhoneNumberField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_phone_number);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();

        sendOTPBtn.setOnClickListener(this);
    }

    public void initializeViews()
    {
        sendOTPBtn = findViewById(R.id.sendOTPbtn);
        carrierPhoneNumberField = findViewById(R.id.phoneField);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        // register the carrier number input field with the cc picker to combine then, giving a complete phone number
        countryCodePicker.registerCarrierNumberEditText(carrierPhoneNumberField);

    }
}