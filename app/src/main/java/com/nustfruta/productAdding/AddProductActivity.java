package com.nustfruta.productAdding;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nustfruta.R;
import com.nustfruta.models.ProductDB;
import com.nustfruta.utility.Constants;
import com.nustfruta.utility.FirebaseDBUtil;
import com.nustfruta.utility.VerifyCredentials;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Objects;

public class AddProductActivity extends AppCompatActivity implements View.OnClickListener, OnFailureListener{

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
        else if (v.getId() == backButton.getId())
        {
            finish();
        }

    }


    public String creationTimestamp;

    TextInputEditText productName, unitPrice, unit;

    TextInputLayout categoryLayout, unitLayout, nameLayout, priceLayout;

    AutoCompleteTextView category;

    Button productSaveBtn, imgSelectBtn, cropBtn;

    ImageView productImage;

    TextView addNewProduct;

    CropImageView cropImageView;

    Uri imageUri, croppedImgUri;

    StorageReference storageReference;

    ImageView backButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproducts);

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.productAddLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeViews();
        attachListeners();
        category.setAdapter(new ArrayAdapter<>(this, R.layout.dropdownitem_layout, Constants.PRODUCT_CATEGORIES));
        creationTimestamp = LocalDateTime.now().toString();
    }

    public void attachListeners()
    {
        productSaveBtn.setOnClickListener(this);
        imgSelectBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        backButton.setOnClickListener(this);
        category.setAdapter(new ArrayAdapter<>(this, R.layout.dropdownitem_layout, Constants.PRODUCT_CATEGORIES));

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
        backButton = findViewById(R.id.backIcon);
        unit = findViewById(R.id.productUnit);

    }


    public void saveProduct() {

        boolean isProductValid = true;

        String inputProductName = productName.getText().toString().toLowerCase().trim();
        String inputUnitPrice =  unitPrice.getText().toString().trim();
        String inputCategory = (category.getText().toString()).trim();
        String inputUnit = (unit.getText().toString().toLowerCase()).trim();

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
            categoryLayout.setEndIconVisible(false);
        }
        else {
            category.setError(null);
            categoryLayout.setEndIconVisible(true);
        }
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

        // when saving the image to firebase storage, and realtime database, bundle the product name with creation time stamp (only for image URL, not product name)
        // to add uniqueness and reset cache for that image.
        uploadImage(inputProductName + creationTimestamp);

        String inputImageUrl = inputCategory.toLowerCase() + "/" + inputProductName + creationTimestamp;

        ProductDB newProduct = new ProductDB(inputProductName, inputUnit, Integer.parseInt(inputUnitPrice), inputImageUrl);

        FirebaseDBUtil.storeProductDB(newProduct, inputCategory);

        Snackbar.make(productSaveBtn,"Product Added Successfully.",Snackbar.LENGTH_SHORT).setBackgroundTint(Constants.COLOR_PRIMARY).addCallback(new Snackbar.Callback(){
            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
                finish();
            }
        }).show();

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

            if (!VerifyCredentials.isImageValidSize(this, data.getData(), Constants.MAX_IMAGE_SIZE))
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


        if ((category.getText().toString()).equals("Fruits"))
            storageReference = FirebaseStorage.getInstance().getReference("fruits/" + productName);

        else if ((category.getText().toString()).equals("Salads"))
            storageReference = FirebaseStorage.getInstance().getReference("salads/" + productName);

        else if ((category.getText().toString()).equals("Juices"))
            storageReference = FirebaseStorage.getInstance().getReference("juices/" + productName);

        storageReference.putFile(croppedImgUri).addOnFailureListener(this);
    }


    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(this, "Image upload failed.", Toast.LENGTH_LONG).show();
    }


    // Helper function to convert bitmap to uri.
    public Uri bitmapToUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
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


}

