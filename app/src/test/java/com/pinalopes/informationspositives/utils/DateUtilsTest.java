package com.pinalopes.informationspositives.utils;

import android.util.Log;

import com.pinalopes.informationspositives.utils.DateUtils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    private static final String TAG = "DateUtilsTest";

    @Test
    public void getDateFromStringTest() {
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        Date date = DateUtils.getDateFromString(1997, 9, 17, format);
        assertNotNull(date);
        try {
            Date date1 = format.parse("17101997");
            assertEquals(date, date1);
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
        }
    }

    @Test
    public void getDateFromStringErrorTest() {
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyySS", Locale.getDefault());
        Date date = DateUtils.getDateFromString(1997, 9, 17, format);
        assertNull(date);
    }

    @Test
    public void dateIsGreaterTest() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = format.parse("17/10/2007");
            Date date1 = format.parse("17/10/1997");
            assertNotNull(date);
            assertNotNull(date1);
            assertTrue(DateUtils.dateIsGreater(date, date1));
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
        }
    }

    @Test
    public void dateIsGreaterErrorTest() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = format.parse("17/10/1997");
            Date date1 = format.parse("17/10/2007");
            assertNotNull(date);
            assertNotNull(date1);
            assertFalse(DateUtils.dateIsGreater(date, date1));
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
        }
    }

    @Test
    public void dateIsLowerTest() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = format.parse("17/10/1997");
            Date date1 = format.parse("17/10/2007");
            assertNotNull(date);
            assertNotNull(date1);
            assertTrue(DateUtils.dateIsLower(date, date1));
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
        }
    }

    @Test
    public void dateIsLowerErrorTest() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = format.parse("17/10/2007");
            Date date1 = format.parse("17/10/1997");
            assertNotNull(date);
            assertNotNull(date1);
            assertFalse(DateUtils.dateIsLower(date, date1));
        } catch (ParseException pe) {
            Log.e(TAG, pe.toString());
        }
    }
}