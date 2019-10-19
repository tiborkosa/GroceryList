package com.example.grocerylist.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.grocerylist.R;

public class MeasureUtil {

    private static Context context = MyApplication.getAppContext();

    public static String[] getMeasurements(){

        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(context);

        String[] array = null;
        String measureString = sharedPreferences.
                getString(context.
                        getResources().
                        getString(R.string.pref_measure_key), null);

        if(measureString != null) {
            try {
                int id = context.
                        getResources().
                        getIdentifier(measureString, "array",
                                MyApplication
                                        .getAppContext()
                                        .getPackageName()
                        );
                array =  context.getResources().getStringArray(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array == null ? getDefault() : array;
    }

    private static String[] getDefault(){
        return context.getResources().getStringArray(R.array.unit_measure_imperial);
    }
}
