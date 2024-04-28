package com.nustfruta.utility;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



}
