package com.plastic.bevslch.europool2016.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.plastic.bevslch.europool2016.SoccerPoolApplication;

/**
 * Created by Oleksiy on 6/2/2016.
 */

public class PreffHelper {
    private static PreffHelper instance;
    public static final String SETTINGS_FILENAME = "soccer_pool_preffs";
    public static final String KEY_EMAIL = "preff_email";
    public static final String KEY_TOKEN = "preff_token";
    private SharedPreferences settings;

    private PreffHelper(Context context) {
        settings = context.getSharedPreferences(SETTINGS_FILENAME, 0);
    }

    public static PreffHelper getInstance() {
        if (instance == null)
            instance = new PreffHelper(SoccerPoolApplication.getContext());
        return instance;
    }

    public void setEmail(String email) {
        settings.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        return settings.getString(KEY_EMAIL, null);
    }

    public void setToken(String token) {
        settings.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return settings.getString(KEY_TOKEN, null);
    }
}
