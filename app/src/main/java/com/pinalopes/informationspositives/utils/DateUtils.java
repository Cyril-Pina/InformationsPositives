package com.pinalopes.informationspositives.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String TAG = "DateUtils";

    private static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

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

    public static boolean isDateLaterThan(int nbDays, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -nbDays);
        Date dateBeforeNbDays = cal.getTime();
        return dateIsLower(date, dateBeforeNbDays);
    }

    public static String dateToTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_TIMESTAMP_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static Date timestampToDate(String timestamp) throws ParseException {
        return new SimpleDateFormat(DEFAULT_TIMESTAMP_FORMAT, Locale.getDefault()).parse(timestamp);
    }
}
