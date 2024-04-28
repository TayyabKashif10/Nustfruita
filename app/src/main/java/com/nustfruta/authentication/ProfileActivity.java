package com.nustfruta.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.nustfruta.utility.Constants;
import com.nustfruta.R;
import com.nustfruta.utility.FirebaseUtil;
import com.nustfruta.utility.VerifyCredentials;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {

        if (v.getId() == saveBtn.getId())
        {
            saveProfile();
        } else if (v.getId() == hostel.getId()) {

            // remove error from hostel text field because it doesn't remove it automatically like other EditText views
            if (hostel.getError() != null)
            {
                hostel.setError(null);
                hostelFieldContainer.setEndIconVisible(true);
            }
        } else if (v.getId() == roomNumber.getId()) {

            String hostelText = (hostelFieldContainer.getEditText()).getText().toString();
            if (hostelText.isEmpty())
            {
                roomNumber.setError("Select the hostel first.");
            }
        }

    }

    EditText fullName, email, roomNumber;

    TextInputLayout hostelFieldContainer;
    AutoCompleteTextView hostel;
    Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profileScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        saveBtn.setOnClickListener(this);
        hostel.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdownitem_layout, Constants.hostelNames));
        hostel.setOnClickListener(this);
        roomNumber.setOnClickListener(this);
    }

    public void initializeViews()
    {
        fullName = findViewById(R.id.name);
        email = findViewById(R.id.email);
        hostel = findViewById(R.id.hostelField);
        roomNumber = findViewById(R.id.room);
        saveBtn = findViewById(R.id.saveBtn);
        hostelFieldContainer = findViewById(R.id.hostelFieldContainer);
    }

    public void saveProfile()
    {

        // this is for the possibility where the user has selected room number first, then hostel and the error doesnt go away (from the last time save button was called)
        roomNumber.setError(null);


        boolean validDataEntered = true;
        // this should never execute because the user should not be able to get to this activity without registering a phone number first.
        // if a guest acount is implemented, make him register a phone number first before getting to this activity.

        if (FirebaseUtil.getCurrentUserID() == null)
        {
            Toast.makeText(getApplicationContext(), "You need to be registered with a Phone number to save a profile.", Toast.LENGTH_LONG).show();
            return;
        }

        String inputName = fullName.getText().toString();
        String inputEmail = email.getText().toString();
        String inputRoomNumber = roomNumber.getText().toString();

        // returns empty string if nothing is selected, and item string otherwise.
        String inputHostel = (hostelFieldContainer.getEditText()).getText().toString();

        if (!inputName.isEmpty() && !VerifyCredentials.verifyFullName(inputName))
        {
            validDataEntered = false;
            fullName.setError("Name must be at least 5 characters.");
        }

        if (!inputEmail.isEmpty() && !VerifyCredentials.verifyEmail(inputEmail))
        {
            validDataEntered = false;
            email.setError("Invalid Email Address");
        }

        if (!inputHostel.isEmpty() && !VerifyCredentials.verifyHostel(inputHostel))
        {
            validDataEntered = false;
            hostel.setError("Please select a hostel.");
            hostelFieldContainer.setEndIconVisible(false);
        }

        if (!inputRoomNumber.isEmpty() && !VerifyCredentials.verifyRoomNumber(inputRoomNumber))
        {
            validDataEntered = false;
            roomNumber.setError("Invalid Room Number");
        }

        if (inputHostel.isEmpty() && !inputRoomNumber.isEmpty())
        {
            validDataEntered = false;
            roomNumber.setError("Select Hostel First");
        }

        if (!validDataEntered)
        {
            return;
        }


        Snackbar.make(saveBtn,"Saved Successfully.",Snackbar.LENGTH_SHORT).show();

        FirebaseUtil.currentUserObject.setEmail(inputEmail);
        FirebaseUtil.currentUserObject.setFullName(inputName);
        FirebaseUtil.currentUserObject.setHostelAddress(inputHostel + "." +inputRoomNumber);

        //over-write the user with the new details.
        FirebaseUtil.storeUser(FirebaseUtil.currentUserObject, FirebaseUtil.getCurrentUserID());

        Snackbar.make(saveBtn,"Saved Successfully.",Snackbar.LENGTH_SHORT).show();
    }


}