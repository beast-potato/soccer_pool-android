package com.plastic.bevslch.europool2016.Helpers;

import com.plastic.bevslch.europool2016.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by omarbs on 2016-06-03.
 */
public class Utilities {

    private static final String TAG = Utilities.class.getSimpleName();

    public static boolean isBeforeNow(Long date) {
        Date now = new Date();
        Date otherDate = new Date(date * 1000);
        return otherDate.before(now);
    }

    // We only check this if the start time is before now (starttime + avg duration > now, inprogress = true)
    public static boolean isMatchInProgress(Long date) {
        Date now = new Date();
        Date otherDate = new Date(date * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(otherDate);
        cal.add(Calendar.MINUTE, Constants.matchAverageDuration);
        return otherDate.after(now);
    }

    public static String formatDate(Long date) {
        SimpleDateFormat formatUI = new SimpleDateFormat(Constants.uiDatePattern, Locale.CANADA);
        Date dateObj = new Date(date * 1000);
        return formatUI.format(dateObj);
    }
}
