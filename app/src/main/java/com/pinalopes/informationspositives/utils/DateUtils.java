package com.pinalopes.informationspositives.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.pinalopes.informationspositives.Constants.ARTICLE_DATE_FORMAT;
import static com.pinalopes.informationspositives.Constants.DATE_FORMAT_FILTER;
import static com.pinalopes.informationspositives.Constants.DEFAULT_TIMESTAMP_FORMAT;
import static com.pinalopes.informationspositives.Constants.MILLIS_IN_A_DAY;
import static com.pinalopes.informationspositives.Constants.NEWS_RESULTS_DATE_FORMAT;
import static com.pinalopes.informationspositives.Constants.NEWS_SEARCHES_DATE_FORMAT;

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

    public static String getActualDate() {
        return new SimpleDateFormat(NEWS_SEARCHES_DATE_FORMAT, Locale.getDefault()).format(new Date().getTime());
    }

    public static String getDateToSearch(String initialDate) {
        try {
            Date date = new SimpleDateFormat(DATE_FORMAT_FILTER, Locale.getDefault()).parse(initialDate);
            return new SimpleDateFormat(NEWS_SEARCHES_DATE_FORMAT, Locale.getDefault()).format(date);
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
        }
        return "";
    }

    public static String getPreviousDate() {
        return new SimpleDateFormat(NEWS_SEARCHES_DATE_FORMAT, Locale.getDefault()).format(new Date().getTime() - MILLIS_IN_A_DAY);
    }

    public static String formatArticlePublishedDate(String publishedDate) {
        SimpleDateFormat articleDateFormat = new SimpleDateFormat(ARTICLE_DATE_FORMAT, Locale.getDefault());
        try {
            Date date = new SimpleDateFormat(NEWS_RESULTS_DATE_FORMAT, Locale.getDefault()).parse(publishedDate);
            assert date != null;
            return articleDateFormat.format(date);
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
        }
        return articleDateFormat.format(new Date());
    }
}
