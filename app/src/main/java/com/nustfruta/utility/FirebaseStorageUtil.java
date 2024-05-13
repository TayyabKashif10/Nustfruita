package com.nustfruta.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nustfruta.R;

import java.io.File;
import java.io.IOException;

abstract public class FirebaseStorageUtil {

    static FirebaseStorage storage;

    static
    {
        storage = FirebaseStorage.getInstance();
    }

    public static StorageReference getFruitImagesReference()
    {
        return storage.getReference("fruits");
    }


    public static void BindImage(ImageView container, String completeImageURL, Context context)
    {
        Glide.with(context).load(storage.getReference(completeImageURL)).placeholder(R.drawable.fruit_apple).error(R.drawable.fruit_pear).into(container);
    }

    public static void deleteImage(String completeImageURL)
    {
        FirebaseStorage.getInstance().getReference(completeImageURL).delete();
    }

}
