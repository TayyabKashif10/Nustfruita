package com.nustfruta.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.nustfruta.utility.Constants;
import com.nustfruta.R;
import com.nustfruta.models.User;
import com.nustfruta.models.UserType;
import com.nustfruta.utility.FirebaseUtil;

import java.util.concurrent.TimeUnit;

public class LoginOTPActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View v) {

        if (v.getId() == resendText.getId())
        {
            sendOTP(phoneNumber, true);
            resetResendText();
        } else if (v.getId() == verifyOTPBtn.getId())
        {
            String inputOTP = otpField.getText().toString();

            // use the actual required OTP data and use entered OTP to generate a credential that can be used to log the user
            // in the database (i.e sign in) the credential could be valid or invalid at this point
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationOTP, inputOTP);
            signIn(credential);
        }
    }

    EditText otpField;

    Button verifyOTPBtn;

    TextView resendText;

    FirebaseAuth mAuth;

    String phoneNumber;

    String verificationOTP;
    PhoneAuthProvider.ForceResendingToken resendingToken;


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
        phoneNumber = getIntent().getExtras().getString("phoneNumber");
        initializeViews();
        mAuth = FirebaseAuth.getInstance();
        verifyOTPBtn.setOnClickListener(this);
        resendText.setOnClickListener(this);
        resetResendText();
        sendOTP(phoneNumber, false);
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
    public void sendOTP(String phoneNumber, boolean isResend)
    {
        PhoneAuthOptions.Builder builder = new PhoneAuthOptions.Builder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(Constants.OTP_TIMEOUT, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                    //OTP is added automatically
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        //TODO: Sign the user in the app
                        Toast.makeText(LoginOTPActivity.this, "Verification Completed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        //TODO: Tell user that the verification has been failed.
                        Toast.makeText(LoginOTPActivity.this, "Verification Failed", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        resendingToken = forceResendingToken;
                        verificationOTP = s;
                        Toast.makeText(LoginOTPActivity.this, "OTP sent", Toast.LENGTH_LONG).show();
                    }
                });

        if (isResend)
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }
        else
        {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    public void signIn(PhoneAuthCredential phoneAuthCredential)
    {
        // register the user with firebase authentication
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    FirebaseUtil.storeUser(new User(UserType.CUSTOMER, task.getResult().getUser().getPhoneNumber(), "","","",""), task.getResult().getUser().getUid());

                    Intent intent = new Intent(LoginOTPActivity.this, ProfileActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Snackbar.make(verifyOTPBtn, "Verified OTP", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    otpField.setError("Invalid OTP");
                }
            }
        });

    }

}