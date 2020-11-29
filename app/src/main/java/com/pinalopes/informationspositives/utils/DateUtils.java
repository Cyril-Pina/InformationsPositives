package com.pinalopes.informationspositives.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String TAG = "DateUtils";

    private DateUtils() {
        throw new AssertionError();
    }

    public static Date getDateFromString(int year, int monthOfYear, int dayOfMonth, SimpleDateFormat format) {
        try {
            String stringBuilder = dayOfMonth
                    + String.format(Locale.getDefault(),"%02d", monthOfYear + 1)
                    + year;
            return format.parse(stringBuilder);
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
            return null;
        }
    }

    public static boolean dateIsGreater(Date firstDate, Date secondDate) {
        return firstDate.compareTo(secondDate) > 0;
    }

    public static boolean dateIsLower(Date firstDate, Date secondDate) {
        return firstDate.compareTo(secondDate) < 0;
    }
}
