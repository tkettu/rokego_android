package com.teroki.rokego_helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Tero on 12.10.2017.
 */

public class DateHelper {

    private final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    /**
     * Format date as milliseconds to default dateformat
     * @param milliseconds
     * @return date as default format
     */
    public String getDate(long milliseconds){
        return getDate(milliseconds, DEFAULT_DATE_FORMAT);
    }

    /**
     * Format date as user format
     * @param milliseconds
     * @param dateFormat
     * @return
     */
    public String getDate(long milliseconds, String dateFormat){
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return formatter.format(calendar.getTime());
    }
}
