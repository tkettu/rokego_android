package com.teroki.rokego_helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Helper class for time and date handling
 * Created by Tero on 12.10.2017.
 */

public class DateHelper {

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Format date as milliseconds to default dateformat
     * @param milliseconds
     * @return date as default format
     */
    public static String getDate(long milliseconds){
        return getDate(milliseconds, DEFAULT_DATE_FORMAT);
    }

    /**
     * Format date as user format
     * @param milliseconds
     * @param dateFormat
     * @return
     */
    public static String getDate(long milliseconds, String dateFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());
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
        int[] timeR = {0, 00, 00};
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
