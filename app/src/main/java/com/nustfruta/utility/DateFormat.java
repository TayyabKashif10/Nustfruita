package com.nustfruta.utility;

import java.util.Calendar;

public class DateFormat {
    public static String EEE_DDMMYY(Calendar date) {
        return String.format("%s, %02d/%02d/%02d",
                dayToString(date.get(Calendar.DAY_OF_WEEK)),
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.MONTH) + 1,   // +1 as Calendar months begin from 0
                date.get(Calendar.YEAR) % 100); // getting last two digits of year for YY
    }

    public static String DDMMYY(Calendar date) {
        return String.format("%02d/%02d/%02d",
                date.get(Calendar.DAY_OF_MONTH),
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.YEAR) % 100);
    }

    private static String dayToString(int day) {
        switch (day) {
            case 1:
                return "Sun";
            case 2:
                return "Mon";
            case 3:
                return "Tue";
            case 4:
                return "Wed";
            case 5:
                return "Thu";
            case 6:
                return "Fri";
            case 7:
                return "Sat";
            default:
                throw new IllegalArgumentException("Day must be between 1 to 7");
        }
    }
}
