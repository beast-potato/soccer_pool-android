package com.plastic.bevslch.europool2016.Helpers;

import android.util.Log;

import com.plastic.bevslch.europool2016.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by omarbs on 2016-06-03.
 */
public class Utilities {

    private static final String TAG = Utilities.class.getSimpleName();

    public static boolean isBeforeNow(String date) {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat(Constants.serverDatePattern, Locale.CANADA);
        try {
            Date otherDate = format.parse(date);
            return otherDate.before(now);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + date);
            return false;
        }
    }

    public static String formatDate(String date) {
        SimpleDateFormat formatServer = new SimpleDateFormat(Constants.serverDatePattern, Locale.CANADA);
        SimpleDateFormat formatUI = new SimpleDateFormat(Constants.uiDatePattern, Locale.CANADA);
        try {
            Date dateObj = formatServer.parse(date);
            return formatUI.format(dateObj);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + date);
            return date;
        }
    }
}