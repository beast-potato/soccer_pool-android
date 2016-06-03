package com.plastic.bevslch.europool2016;

import android.app.Application;
import android.content.Context;

/**
 * Created by Oleksiy on 6/2/2016.
 */

public class SoccerPoolApplication extends Application {
    private static SoccerPoolApplication instance;

    public static SoccerPoolApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
