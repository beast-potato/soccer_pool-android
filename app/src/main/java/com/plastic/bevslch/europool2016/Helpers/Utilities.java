package com.plastic.bevslch.europool2016.Helpers;

import com.plastic.bevslch.europool2016.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by omarbs on 2016-06-03.
 */
public class Utilities {

    private static final String TAG = Utilities.class.getSimpleName();

    public static String formatDate(Long date) {
        SimpleDateFormat formatUI = new SimpleDateFormat(Constants.uiDatePattern, Locale.CANADA);
        Date dateObj = new Date(date * 1000);
        return formatUI.format(dateObj);
    }
}
