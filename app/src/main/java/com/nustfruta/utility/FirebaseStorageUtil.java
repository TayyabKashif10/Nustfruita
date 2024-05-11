package com.nustfruta.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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


    public static void BindImage(ImageView imageView, String completeImageURL)
    {
        try {
            File localFile = File.createTempFile("tempfile",completeImageURL.substring(completeImageURL.length() - 3));
            storage.getReference(completeImageURL).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //TODO: if showing a progress dialog or something, handle that
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);

                }
            });
        } catch (IOException e) {
            Log.e("DBError" , "Failed to download image" , e);
        }
    }

    public static void deleteImage(String completeImageURL)
    {
        FirebaseStorage.getInstance().getReference(completeImageURL).delete();
    }

}
