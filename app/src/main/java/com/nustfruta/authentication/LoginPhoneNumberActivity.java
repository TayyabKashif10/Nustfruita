package com.nustfruta.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.nustfruta.R;
import com.nustfruta.menu.MenuActivity;
import com.nustfruta.models.UserType;
import com.nustfruta.dialog.DialogFactory;
import com.nustfruta.utility.FirebaseDBUtil;

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
        } else if (v.getId() == guestText.getId()) {

            startAppAsGuest();
        }
    }


    Button sendOTPBtn;
    CountryCodePicker countryCodePicker;
    EditText carrierPhoneNumberField;

    TextView guestText;

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
        guestText.setOnClickListener(this);
    }

    public void initializeViews()
    {
        sendOTPBtn = findViewById(R.id.sendOTPbtn);
        carrierPhoneNumberField = findViewById(R.id.phoneField);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        // register the carrier number input field with the cc picker to combine then, giving a complete phone number
        countryCodePicker.registerCarrierNumberEditText(carrierPhoneNumberField);
        guestText = findViewById(R.id.guestText);

    }

    private void startAppAsGuest() {

        //Directly start new activity instead of imposing new Guest account, this is because an anonymous account stays logged in, like regular accounts.
        if (FirebaseDBUtil.isAnonymousUserLoggedIn())
        {
             //shift to main activity
             Intent intent = new Intent(LoginPhoneNumberActivity.this, MenuActivity.class);
             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent);
        }
        else
        {
            DialogFactory.createLoadingDialog(this, false);
            FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(task -> {

                FirebaseDBUtil.currentUserType = UserType.GUEST;

                //shift to main activity
                Intent intent = new Intent(LoginPhoneNumberActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);

                DialogFactory.destroyLoadingDialog();
                startActivity(intent);

            });
        }

    }

}