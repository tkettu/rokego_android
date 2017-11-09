package com.teroki.rokego_helpers;

import com.teroki.interfaces.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper class for time and date handling
 * Created by Tero on 12.10.2017.
 */

public class DateHelper {

    //private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    private static final String DEFAULT_DATE_FORMAT = Constants.DATE_FORMAT.DEFAULT_FORMAT;

    /**
     * Format date as milliseconds to default dateformat
     * @param milliseconds Date as milliseconds
     * @return date as default format
     */
    public static String getDate(long milliseconds){
        return getDate(milliseconds, DEFAULT_DATE_FORMAT);
    }

    /**
     * Format date as user format
     * @param milliseconds Date as milliseconds
     * @param dateFormat Format of date (ex. 'dd/MM/yyyy')
     * @return Formatted date
     */
    public static String getDate(long milliseconds, String dateFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());
    }

    /**
     * Convert date string to milliseconds
     * @param date As default format (ex. "dd/MM/yyyy")
     * @return date as milliseconds
     */

    public static long dateToMillis(String date){
        return dateToMillis(date, DEFAULT_DATE_FORMAT);
    }

    public static long dateToMillis(String date, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date d;
        try {
            d = sdf.parse(date);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }

    }


    /**
     * Changes hours (h.hhh) to hh:mm(:ss)
     * @param hours As decimal hours
     * @return Time as readable format
     */
    public static String hoursToTime(double hours){
        //String time = "0:0";
        int h = (int) hours; // Hole hours

        double remainder = (hours - (double)h); //remainder of hours
        int mm = (int)Math.round(remainder * 60); //Hole minutes rounded by seconds

        return String.valueOf(h) + ":" + (mm>10 ? String.valueOf(mm) : "0" + String.valueOf(mm));
    }

    /**
     * Format time to 3 length array
     * @param time As format hh:mm:ss or mm:ss
     * @return size 3 array format [hh,mm,ss]
     */
    public static int[] timeToArray(String time){
        String[] timeA = time.split(":");
        int[] timeR = {0, 0, 0};
        //timeR[0] = (timeA.length == 3 ? Integer.parseInt(timeA[0]): 0);
        if (timeA.length == 3){
            timeR[0] = Integer.parseInt(timeA[0]);
            timeR[1] = Integer.parseInt(timeA[1]);
            timeR[2] = Integer.parseInt(timeA[2]);
        }else {
            timeR[0] = 0;
            timeR[1] = Integer.parseInt(timeA[0]);
            timeR[2] = Integer.parseInt(timeA[1]);
        }

        return timeR;
    }

    public static String concatTime(int hours, int minutes, int seconds) {
        String time = "";
        String h, m, s;
        h = String.valueOf(hours);
        m = (minutes >= 10 ? String.valueOf(minutes) : "0"+ String.valueOf(minutes));
        s = (seconds >= 10 ? String.valueOf(seconds) : "0"+ String.valueOf(seconds));

        return (h + ":" + m + ":" + s);
    }

    public static String concatTime(String hours, String minutes, String seconds) {
        String m, s;
        m = (minutes.length() >= 2 ? minutes : "0"+minutes);
        s = (seconds.length() >= 2 ? seconds : "0"+ seconds);
        return (hours + ":" + m + ":" + s);
    }
}
