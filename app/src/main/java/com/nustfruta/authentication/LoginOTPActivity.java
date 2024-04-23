package com.nustfruta.authentication;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nustfruta.R;

public class LoginOTPActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {

        if (v.getId() == resendText.getId())
        {
            //TODO: actually send the OTP
            resetResendText();
        }
    }

    EditText otpField;

    Button verifyOTPBtn;

    TextView resendText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        verifyOTPBtn.setOnClickListener(this);
        resendText.setOnClickListener(this);
        resetResendText();
    }

    public void initializeViews()
    {
        otpField = findViewById(R.id.otpField);
        verifyOTPBtn = findViewById(R.id.verifyOTPBtn);
        resendText = findViewById(R.id.resendText);
    }

    public void resetResendText()
    {
        resendText.setText(R.string.resend_otp_prompt);
        resendText.setEnabled(false);
        CountDownTimer cTimer = new CountDownTimer(30000, 1000) {
            String currentMessage;
            public void onTick(long millisUntilFinished) {
                currentMessage = "Resend OTP after " +String.valueOf(millisUntilFinished/1000) + " seconds";
                resendText.setText(currentMessage);
            }
            public void onFinish() {
                resendText.setText("Resend OTP");
                resendText.setEnabled(true);
            }
        };
        cTimer.start();

    }

}