package com.nustfruta.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nustfruta.dashboard.MenuActivity;
import com.nustfruta.models.User;
import com.nustfruta.utility.Constants;
import com.nustfruta.R;
import com.nustfruta.utility.FirebaseUtil;
import com.nustfruta.utility.VerifyCredentials;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    User currentSignedUser;

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
            String hostelHint = hostel.getHint().toString();
            if (hostelText.isEmpty() && hostelHint.equals("Hostel"))
            {
                roomNumber.setError("Select the hostel first.");
            }
        }

        else if (v.getId() == skipText.getId()) {

            // shift to main activity
            Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    EditText fullName, email, roomNumber;

    TextInputLayout hostelFieldContainer;
    AutoCompleteTextView hostel;

    TextView skipText, profileText;
    Button saveBtn;

    boolean firstTimeUser;


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
        getUser();
        saveBtn.setOnClickListener(this);
        hostel.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdownitem_layout, Constants.hostelNames));
        hostel.setOnClickListener(this);
        roomNumber.setOnClickListener(this);

        firstTimeUser = getIntent().getExtras()!= null && getIntent().getExtras().getString("caller", "unknown").equals("LoginOTPActivity");

        // if its the first time the user has come across profile completion allow user to skip completion, set unique text for Profile Activity.
        if (firstTimeUser)
        {
            skipText.setVisibility(View.VISIBLE);
            skipText.setOnClickListener(this);
            profileText.setText(getString(R.string.complete_profile_prompt));
        }
    }

    public void initializeViews()
    {
        fullName = findViewById(R.id.name);
        email = findViewById(R.id.email);
        hostel = findViewById(R.id.hostelField);
        roomNumber = findViewById(R.id.room);
        saveBtn = findViewById(R.id.productSaveBtn);
        hostelFieldContainer = findViewById(R.id.hostelFieldContainer);
        skipText = findViewById(R.id.skipText);
        profileText = findViewById(R.id.profileText);
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
        String inputHostelHint = hostel.getHint().toString();

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

        if (inputHostelHint.equals("Hostel")&& inputHostel.isEmpty() && !inputRoomNumber.isEmpty())
        {
            validDataEntered = false;
            roomNumber.setError("Select Hostel First");
        }

        if (!validDataEntered)
        {
            return;
        }

        Snackbar.make(saveBtn,currentSignedUser.getPhoneNumber(),Snackbar.LENGTH_SHORT).show();

        currentSignedUser.setEmail(inputEmail);
        currentSignedUser.setFullName(inputName);

        /*
        * For the extended drop down menu text field, we use both the hint and the text as user input.
        * the hint is by default "Hostel". if the hint remains Hostel, that means the user didnt select anything
        * and the hostel should be saved as empty, which is what getText() will return, so we set that.
        *
        * if the hint is not hostel, that means the previously stored value of hostel in database is the hint, so it should
        * just propagate that, the getText() method will return an empty string here.
        *
        * if hint is empty, that means it has been replaced by user selected text, so just use that.
        *
        * Now the question is, why use hints and texts both and create this mess? Because we want to popualte the field
        * with the previously stored database value. if we do that with setText(), the drop down part of the field stops working for some reason
        * so previously stored values are saved as hints, and user selected are saved as text.
        * */

        if (!inputHostel.isEmpty())
        {
            currentSignedUser.setHostel(inputHostel);
        }


        currentSignedUser.setRoomNumber(inputRoomNumber);

        //over-write the user with the new details.
        FirebaseUtil.storeUser(currentSignedUser, FirebaseUtil.getCurrentUserID());

        Snackbar.make(saveBtn,"Saved Successfully.",Snackbar.LENGTH_SHORT).show();


        // shift to main activity
        Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }




    public void getUser()
    {

        FirebaseUtil.getCurrentUserReference().addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentSignedUser = snapshot.getValue(User.class);
                        populateProfileFields();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }

    public void populateProfileFields()
    {
        fullName.setText(currentSignedUser.getFullName());
        email.setText(currentSignedUser.getEmail());

        // if no hostel has been saved, display the default hint "Hostel", displaying the empty text
        // as hint will leave the field empty. if its not empty, display the previously selected hostel as the hint.
        if (!currentSignedUser.getHostel().isEmpty())
        {
            hostel.setHint(currentSignedUser.getHostel());
        }

        roomNumber.setText(currentSignedUser.getRoomNumber());
    }

}