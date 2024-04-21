package com.nustfruta.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyCredentials {

    public static boolean verifyPhone(String phoneNumber)
    {

        String validPhoneRegex = "^(\\+92|92|0)?\\d{10}$";
        Pattern pattern = Pattern.compile(validPhoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean verifyEmail(String email)
    {
        String validEmailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(validEmailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean verifyPassword(String password)
    {
        // This regex pattern enforces a password to have at least 8 characters, with at least one uppercase letter, one lowercase letter, one digit, and one special character.
        String validPasswordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$\n";
        Pattern pattern = Pattern.compile(validPasswordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }



}
