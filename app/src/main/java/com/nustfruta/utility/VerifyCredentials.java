package com.nustfruta.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public abstract class VerifyCredentials {

    public static boolean verifyFullName(String name)
    {
        return name.length() >= Constants.MIN_NAME_LENGTH;
    }

    public static boolean verifyEmail(String email)
    {
      return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean verifyRoomNumber(String room)
    {
        return !room.isEmpty() && Integer.parseInt(room) <= Constants.HOSTEL_UPPER_ROOM_LIMIT && Integer.parseInt(room) >= Constants.HOSTEL_LOWER_ROOM_LIMIT;
    }

    public static boolean verifyHostel(String hostel)
    {
        return !hostel.isEmpty();
    }



    public static boolean isImageValidSize(Context context, Uri imageUri, long maxSize) {


        try {

            Cursor cursor = context.getContentResolver().query(imageUri, new String[]{MediaStore.Images.Media.SIZE}, null, null, null);


            if (cursor != null && cursor.moveToFirst()) {

                @SuppressLint("Range") long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                cursor.close();

                if (size != 0)
                    return size <= maxSize;
            }
        }

        catch (Exception e) {
            Log.e("TAG", "Error getting image size:", e);
        }

        return false;
    }



}
