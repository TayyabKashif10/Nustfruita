package com.nustfruta.productAdding;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nustfruta.R;
import com.nustfruta.models.ProductDB;
import com.nustfruta.utility.FirebaseDBUtil;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class addProducts extends AppCompatActivity implements View.OnClickListener, OnFailureListener, AdapterView.OnItemSelectedListener {



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

        else if (v.getId() == cropBtn.getId())
        {

            croppedImgUri = bitmapToUri(getApplicationContext(), Objects.requireNonNull(cropImageView.getCroppedImage()));
            productAddLayout();

            if(croppedImgUri != null) {
                productImage.setImageURI(croppedImgUri);

                cropBtn.setError(null);
            }

            else
                Toast.makeText(this, "Image upload failed.", Toast.LENGTH_LONG).show();


        }

    }


    //    Product newProduct;
    TextInputEditText productName, unitPrice;

    TextInputLayout categoryLayout, unitLayout, nameLayout, priceLayout;

    AutoCompleteTextView category, unit;

    Button productSaveBtn, imgSelectBtn, cropBtn;

    ImageView productImage;

    TextView addNewProduct;

    CropImageView cropImageView;


    Uri imageUri, croppedImgUri;

    StorageReference storageReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_addproducts);

        initializeViews();

        productSaveBtn.setOnClickListener(this);
        imgSelectBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        category.setOnItemSelectedListener(this);
        unit.setOnItemSelectedListener(this);


        category.setAdapter(new ArrayAdapter<>(this, R.layout.dropdownitem_layout, new String[]{"Fruit", "Salad"}));
        unit.setAdapter(new ArrayAdapter<>(this, R.layout.dropdownitem_layout, new String[]{"Kg", "Dozen", "Piece"}));

    }


    public void initializeViews() {

        productName = findViewById(R.id.productName);
        unitPrice = findViewById(R.id.unitPrice);
        productImage = findViewById(R.id.productImage);
        imgSelectBtn = findViewById(R.id.imgSelectBtn);
        productSaveBtn = findViewById(R.id.productSaveBtn);
        addNewProduct = findViewById(R.id.addNewProduct);
        category = findViewById(R.id.category);
        unit = findViewById(R.id.productUnit);
        nameLayout = findViewById(R.id.productNameLayout);
        priceLayout = findViewById(R.id.unitPriceLayout);
        unitLayout = findViewById(R.id.productUnitLayout);
        categoryLayout = findViewById(R.id.categoryLayout);

        cropBtn = findViewById(R.id.cropDoneBtn);
        cropImageView = findViewById(R.id.cropImageView);
        cropImageView.setAspectRatio(1, 1);

    }


    public void saveProduct() {

        boolean isProductValid = true;

        String inputProductName = productName.getText().toString().toLowerCase();
        String inputUnitPrice =  unitPrice.getText().toString();
        String inputCategory = (category.getText().toString());
        String inputUnit = (unit.getText().toString());

        if (croppedImgUri == null)
        {
            isProductValid = false;

            imgSelectBtn.setError("Please select an image");
        }
        else
            imgSelectBtn.setError(null);

        if (inputUnit.isEmpty())
        {
            isProductValid = false;

            unit.setError("Please select a unit");
        }
        else
            unit.setError(null);


        if (inputCategory.isEmpty())
        {
            isProductValid = false;
            category.setError("Please select a category");
        }
        else
            category.setError(null);

        if (inputProductName.isEmpty())
        {
            isProductValid = false;
            productName.setError("Please enter a product name");
        }
        else
            productName.setError(null);


        if (inputUnitPrice.isEmpty() || Integer.parseInt(inputUnitPrice) <= 0)
        {
            isProductValid = false;

            unitPrice.setError("Please enter a valid price");
        }
        else
            unitPrice.setError(null);

        if (!isProductValid)
        {
            return;
        }

        uploadImage(inputProductName);

        String inputImageUrl = storageReference.getDownloadUrl().toString();

        ProductDB newProduct = new ProductDB(inputProductName, inputUnit, Integer.parseInt(inputUnitPrice), inputImageUrl);

        FirebaseDBUtil.storeProductDB(newProduct, inputCategory);

        finish();
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

            if (!isImageValidSize(this, data.getData(), 1024 * 50))
            {
                Toast.makeText(this, "Image size is too large, try again", Toast.LENGTH_LONG).show();

                return;
            }

            imageUri = data.getData();

            cropLayout();

            cropImageView.setImageUriAsync(imageUri);
        }

    }


    public void uploadImage(String productName) {

        // TODO: CONFIRM IF IT WORKS

        if ((category.getText().toString()).equals("Fruit"))
            storageReference = FirebaseStorage.getInstance().getReference("fruits/" + productName);

        else if ((category.getText().toString()).equals("Salad"))
            storageReference = FirebaseStorage.getInstance().getReference("salads/" + productName);

        storageReference.putFile(croppedImgUri).addOnFailureListener(this);
    }


    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, "Image upload failed.", Toast.LENGTH_LONG).show();
    }


    // Helper function to convert bitmap to uri.
    public Uri bitmapToUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void cropLayout() {

        cropImageView.setVisibility(View.VISIBLE);
        cropBtn.setVisibility(View.VISIBLE);


        addNewProduct.setVisibility(View.GONE);
        productName.setVisibility(View.GONE);
        unitPrice.setVisibility(View.GONE);
        productImage.setVisibility(View.GONE);
        imgSelectBtn.setVisibility(View.GONE);
        productSaveBtn.setVisibility(View.GONE);
        category.setVisibility(View.GONE);
        unit.setVisibility(View.GONE);
        nameLayout.setVisibility(View.GONE);
        priceLayout.setVisibility(View.GONE);
        unitLayout.setVisibility(View.GONE);
        categoryLayout.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (view.getId() == categoryLayout.getId())
            categoryLayout.setError(null);

        if (view.getId() == unitLayout.getId())
            unitLayout.setError(null);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void productAddLayout() {

        productName.setVisibility(View.VISIBLE);
        unitPrice.setVisibility(View.VISIBLE);
        productImage.setVisibility(View.VISIBLE);
        imgSelectBtn.setVisibility(View.VISIBLE);
        productSaveBtn.setVisibility(View.VISIBLE);
        addNewProduct.setVisibility(View.VISIBLE);
        category.setVisibility(View.VISIBLE);
        unit.setVisibility(View.VISIBLE);
        nameLayout.setVisibility(View.VISIBLE);
        priceLayout.setVisibility(View.VISIBLE);
        unitLayout.setVisibility(View.VISIBLE);
        categoryLayout.setVisibility(View.VISIBLE);


        cropImageView.setVisibility(View.GONE);
        cropBtn.setVisibility(View.GONE);

    }


    public boolean isImageValidSize(Context context, Uri imageUri, long maxSize) {


        try {

            Cursor cursor = context.getContentResolver().query(imageUri, new String[]{MediaStore.Images.Media.SIZE}, null, null, null);


            if (cursor != null && cursor.moveToFirst()) {

                @SuppressLint("Range") long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                cursor.close();

                if (size != 0)
                    return size <= maxSize;

                else
                    Log.e("TAG", "Error: SIZE column not found in cursor");
            }
        }

        catch (Exception e) {
            Log.e("TAG", "Error getting image size:", e);
        }

        return false;
    }
}

