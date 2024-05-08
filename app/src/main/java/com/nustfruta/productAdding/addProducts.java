package com.nustfruta.productAdding;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nustfruta.R;
import com.nustfruta.models.LegacyProduct;

public class addProducts extends AppCompatActivity implements View.OnClickListener {


    LegacyProduct newProduct;


    @Override
    public void onClick(View v) {

        if (v.getId() == productSaveBtn.getId())
        {
            saveProduct();
        }

        else if (v.getId() == imgSelectBtn.getId())
        {
            selectImage();
        }

    }


    TextInputEditText productName, unitPrice;

    Button productSaveBtn, imgSelectBtn;

    ImageView productImage;

    Uri imageUri;

    StorageReference storageReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addproducts);

        initializeViews();

        productSaveBtn.setOnClickListener(this);
        imgSelectBtn.setOnClickListener(this);


    }


    public void initializeViews() {

        productName = findViewById(R.id.productName);
        unitPrice = findViewById(R.id.unitPrice);
        productImage = findViewById(R.id.productImage);
        imgSelectBtn = findViewById(R.id.imgSelectBtn);
        productSaveBtn = findViewById(R.id.productSaveBtn);

    }


    public void saveProduct() {

        boolean isProductValid = true;

        String inputProductName = productName.getText().toString();
        String inputUnitPrice =  unitPrice.getText().toString();


        if (inputProductName.isEmpty())
        {
           isProductValid = false;
           productName.setError("Please enter a product name");
        }


        if (inputUnitPrice.isEmpty() || Integer.parseInt(inputUnitPrice) <= 0)
        {
            isProductValid = false;
            unitPrice.setError("Please enter a valid price");
        }

        if (!isProductValid)
        {
            return;
        }

        uploadImage(inputProductName);

        // TODO: Terminate the activity here

    }


    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();

            productImage.clearColorFilter();

            productImage.setImageURI(imageUri);
        }

    }




    public void uploadImage(String productName) {


        storageReference = FirebaseStorage.getInstance().getReference("images/" + productName);

        storageReference.putFile(imageUri);

        // Option to use onSuccessListener and onFailureListener here but its so ugly.

        productImage.setImageResource(R.drawable.blue_fruit_cherries);

    }
}
