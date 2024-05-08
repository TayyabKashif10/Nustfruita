package com.nustfruta.productAdding;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nustfruta.R;
import com.nustfruta.models.Product;
import com.nustfruta.utility.Constants;
import com.nustfruta.utility.FirebaseUtil;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class addProducts extends AppCompatActivity implements View.OnClickListener, OnFailureListener, OnSuccessListener {



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

            if(croppedImgUri != null)
                productImage.setImageURI(croppedImgUri);


            else
                Toast.makeText(this, "Image upload failed.", Toast.LENGTH_LONG).show();


        }
        
    }


//    Product newProduct;
    TextInputEditText productName, unitPrice;

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

        category.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdownitem_layout, new String[]{"Fruits", "Salads"}));
        unit.setAdapter(new ArrayAdapter<String>(this, R.layout.dropdownitem_layout, new String[]{"Kilogram", "Dozen", "Piece"}));

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

        cropBtn = findViewById(R.id.cropDoneBtn);
        cropImageView = findViewById(R.id.cropImageView);
        cropImageView.setAspectRatio(1, 1);

        cropBtn.setVisibility(View.GONE);
        cropImageView.setVisibility(View.GONE);

    }


    public void saveProduct() {

        boolean isProductValid = true;

        String inputProductName = productName.getText().toString().toLowerCase();
        String inputUnitPrice =  unitPrice.getText().toString();
        String inputCategory = (category.getText().toString());
        String inputUnit = (unit.getText().toString());

        // Option to add OnSuccess listener but i dont see a need
        String inputImageUrl = storageReference.getDownloadUrl().toString();


        if (!unit.getHint().toString().isEmpty())
        {
            isProductValid = false;
            category.setError("Please select a unit");
        }


        if (!category.getHint().toString().isEmpty())
        {
            isProductValid = false;
            category.setError("Please select a category");
        }

        if (inputProductName.isEmpty())
        {
           isProductValid = false;
           productName.setError("Please enter a product name");
        }

        if (inputImageUrl.isEmpty())
        {
            isProductValid = false;

            imgSelectBtn.setError("Please select an image.");
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


//        newProduct = new Product(inputProductName, inputUnitPrice, inputUnit, inputCategory, inputImageUrl);

//        FirebaseUtil.storeProduct(newProduct);

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

        if ((category.getText().toString()).equals("Fruits"))
            storageReference = FirebaseStorage.getInstance().getReference("fruits/" + productName);

        else if ((category.getText().toString()).equals("Salads"))
            storageReference = FirebaseStorage.getInstance().getReference("salads/" + productName);

        storageReference.putFile(imageUri).addOnFailureListener(this).addOnSuccessListener(this);
    }


    @Override
    public void onFailure(@NonNull Exception e) {

        Toast.makeText(this, "Image upload failed.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(Object o) {
        finish();
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


        cropImageView.setVisibility(View.GONE);
        cropBtn.setVisibility(View.GONE);

    }


    public boolean isImageValidSize(Context context, Uri imageUri, long maxSize) {


        try {

            Cursor cursor = context.getContentResolver().query(imageUri, new String[]{MediaStore.Images.Media.SIZE}, null, null, null);


            if (cursor != null && cursor.moveToFirst()) {

                long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE) + 1);
                cursor.close();

                if (size != 0)
                    return size - 1 <= maxSize;

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

