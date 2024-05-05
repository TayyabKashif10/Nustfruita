package com.nustfruta.utility;

// store static final objects, numbers etc in this class
// string resources should be externalized to strings.xml because those are subject to language translation.

import java.util.ArrayList;
import java.util.Arrays;

abstract public class Constants {
    public static final Long OTP_TIMEOUT = 60L;
    public static final int MIN_NAME_LENGTH = 5;
    public static final Integer HOSTEL_UPPER_ROOM_LIMIT = 430;

    // these lists are not subject to translation, so they are not stored in xml files.
    public static final ArrayList<String> hostelNames = new ArrayList<>(Arrays.asList("Rumi","Johar","Ammar","Ghazali","Beruni","Razi","Rahmat","Attar","Liaquat","Hajveri","Zakariya","Fatima","Zainab","Ayesha","Khadija","Amna"
    ));
    public static final int HOSTEL_LOWER_ROOM_LIMIT = 0;

    public static final int FACT_CHANGE_DELAY = 5*1000; // in milliseconds


    public static final int MENU_CATEGORIES = 3;

    public static final int DELIVERY_FEES = 99;


}
