package com.example.grocerylist.Util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public final class MyDateFormat {
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd yyyy");

    public static String getDate(Calendar date){
        return simpleDateFormat.format(date.getTime());
    }

    public static Calendar getDate(String date){
        Calendar parsedDate = Calendar.getInstance();
        try{
            Date parsed =  simpleDateFormat.parse(date);
            parsedDate.setTime(parsed);
        } catch (ParseException e){
            Log.d(MyDateFormat.class.getSimpleName(), "Date could not be formatted. " + e.getMessage());
        }
        Log.d("DATE", parsedDate.toString());
        return parsedDate;
    }

    public static Calendar getDateNow(Date date){
        Calendar parsedDate = Calendar.getInstance();
        parsedDate.setTime(date);
        return parsedDate;
    }
}
