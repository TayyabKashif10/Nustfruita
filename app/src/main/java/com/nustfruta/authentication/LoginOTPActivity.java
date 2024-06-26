package com.nustfruta.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.nustfruta.menu.AdminMenuActivity;
import com.nustfruta.menu.MenuActivity;
import com.nustfruta.utility.Constants;
import com.nustfruta.R;
import com.nustfruta.models.User;
import com.nustfruta.models.UserType;
import com.nustfruta.dialog.DialogFactory;
import com.nustfruta.utility.FirebaseDBUtil;

import java.util.concurrent.TimeUnit;

public class LoginOTPActivity extends AppCompatActivity implements View.OnClickListener {

     public static boolean  firstTimeUser = true;

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
            if (inputOTP.isEmpty())
            {
                otpField.setError("Invalid OTP");
                return;
            }
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

        // enable OTP button once OTP is sent.
        verifyOTPBtn.setClickable(false);

        overridePendingTransition(com.firebase.ui.auth.R.anim.fui_slide_in_right, com.firebase.ui.auth.R.anim.fui_slide_out_left);
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
                currentMessage = "Resend OTP after " + millisUntilFinished / 1000 + " seconds";
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
        DialogFactory.createLoadingDialog(LoginOTPActivity.this, false);
        PhoneAuthOptions.Builder builder = new PhoneAuthOptions.Builder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(Constants.OTP_TIMEOUT, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                    // this method triggers when OTP is added automatically / autofilled
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                        DialogFactory.destroyLoadingDialog();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.d("AUTH_FAILTURE" , e.getMessage());
                        DialogFactory.destroyLoadingDialog();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        DialogFactory.destroyLoadingDialog();
                        resendingToken = forceResendingToken;
                        verificationOTP = s;
                        Snackbar.make(verifyOTPBtn, "OTP sent", Snackbar.LENGTH_SHORT).setBackgroundTint(Constants.COLOR_PRIMARY).show();
                        verifyOTPBtn.setClickable(true);
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

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())
                {
                    if (!task.getResult().getAdditionalUserInfo().isNewUser())
                    {
                        firstTimeUser = false;
                    }

                    Intent intent;
                    if (firstTimeUser)
                    {
                        FirebaseDBUtil.storeUser(new User(UserType.CUSTOMER, task.getResult().getUser().getPhoneNumber(), "","","",""), task.getResult().getUser().getUid());
                        intent = new Intent(LoginOTPActivity.this, ProfileActivity.class);
                        intent.putExtra("caller", "LoginOTPActivity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                    else
                    {
                        DialogFactory.createLoadingDialog(LoginOTPActivity.this, false);


                        // this triggers if the user has signed in before, decide whether the user is an admin or customer.
                        FirebaseDBUtil.getCurrentUserReference().child("userType").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {

                                FirebaseDBUtil.currentUserType = task.getResult().getValue(UserType.class);
                                Intent intent;
                                if (FirebaseDBUtil.currentUserType == UserType.ADMIN)
                                {

                                    intent = new Intent(LoginOTPActivity.this, AdminMenuActivity.class);
                                }
                                else
                                {
                                    intent = new Intent(LoginOTPActivity.this, MenuActivity.class);
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);

                                DialogFactory.destroyLoadingDialog();
                                startActivity(intent);
                            }

                        });
                    }


                }

                else
                {
                    otpField.setError("Invalid OTP");
                }
            }
        });
    }

}