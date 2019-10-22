package com.example.grocerylist.util;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.grocerylist.R;

/**
 * measurement util to get the required measurements
 * this is needed for the shared preferences
 */
public class MeasureUtil {

    /**
     * getting either the metric or imperial measurements
     * @return
     */
    public static String[] getMeasurements(){

        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(MyApplication.getAppContext());

        String[] array = null;
        String measureString = sharedPreferences.
                getString(MyApplication.getAppContext().
                        getResources().
                        getString(R.string.pref_measure_key), null);

        if(measureString != null) {
            try {
                int id = MyApplication.getAppContext().
                        getResources().
                        getIdentifier(measureString, "array",
                                MyApplication
                                        .getAppContext()
                                        .getPackageName()
                        );
                array =  MyApplication.getAppContext().getResources().getStringArray(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array == null ? getDefault() : array;
    }

    /**
     * at the start we always want to get the imperial measurements
     * @return
     */
    private static String[] getDefault(){
        return MyApplication.getAppContext().getResources().getStringArray(R.array.unit_measure_imperial);
    }
}
