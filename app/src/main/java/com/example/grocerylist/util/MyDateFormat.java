package com.example.grocerylist.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import timber.log.Timber;

/**
 * our special date formatter
 */
public final class MyDateFormat {
    // pattern we want to show the data
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd yyyy");

    /**
     * transforming calendar date to string
     * @param date calendar
     * @return string value
     */
    public static String getDate(Calendar date){
        return simpleDateFormat.format(date.getTime());
    }

    /**
     * transform string date to calendar
     * @param date string value
     * @return parsed calendar date
     */
    public static Calendar getDate(String date){
        Calendar parsedDate = Calendar.getInstance();
        try{
            Date parsed =  simpleDateFormat.parse(date);
            parsedDate.setTime(parsed);
        } catch (ParseException e){
            Timber.d( "Date could not be formatted. %s", e.getMessage());
        }
        Timber.d(parsedDate.toString());
        return parsedDate;
    }

    /**
     * getting the now date
     * @param date of now
     * @return calendar date of now
     */
    public static Calendar getDateNow(Date date){
        Calendar parsedDate = Calendar.getInstance();
        parsedDate.setTime(date);
        return parsedDate;
    }
}
