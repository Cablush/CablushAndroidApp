package com.cablush.cablushapp.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oscar on 13/02/16.
 */
public class DateTimeUtils {

    private static final String TAG = DateTimeUtils.class.getSimpleName();

    public static final DateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm");
    public static final DateFormat FORMAT_DATE = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Parses a date from the specified string using the FORMAT_TIME.
     * <p>If the input is null or any error occur, this method will return the 0 date.</p>
     *
     * @param time
     * @return
     */
    public static Date parseTime(String time) {
        try {
            return FORMAT_TIME.parse(time);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing begin time.");
        }
        return new Date(0);
    }

    /**
     * Formats the specified date using the FORMAT_TIME.
     *
     * @param time
     * @return
     */
    public static String formatTime(Date time) {
        if (time != null) {
            return FORMAT_TIME.format(time);
        }
        return null;
    }

    /**
     * Parses a date from the specified string using the FORMAT_DATE.
     * <p>If the input is null or any error occur, this method will return the 0 date.</p>
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        try {
            return FORMAT_DATE.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing begin time.");
        }
        return new Date(0);
    }

    /**
     * Formats the specified date using the FORMAT_TIME.
     *
     * @param time
     * @return
     */
    public static String formatDate(Date time) {
        if (time != null) {
            return FORMAT_DATE.format(time);
        }
        return null;
    }

    /**
     * Clear time part from a date.
     *
     * @param date
     * @return
     */
    public static Date clearTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
