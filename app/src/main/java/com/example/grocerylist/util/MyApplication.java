package com.example.grocerylist.util;

import android.app.Application;
import android.content.Context;

/**
 * utility class to get the application context
 */
public class MyApplication extends Application {

    private static Context mContext;

    /**
     * on create we set the app context
     */
    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * Getting the app context
     * @return app context
     */
    public static Context getAppContext(){
        return mContext;
    }
}
