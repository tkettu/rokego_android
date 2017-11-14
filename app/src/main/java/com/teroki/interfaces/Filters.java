package com.teroki.interfaces;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tero on 9.11.2017.
 */

public class Filters {
    public interface EXERCISE_FILTERS{
        public static String SPORT_NAME = "";
        public static String SPORT_TYPE = "";

        public static int MIN_DISTANCE = 0; // As kilometers
        public static int MAX_DISTANCE = Integer.MAX_VALUE;
        public static int MIN_TIME = 0;  // As minutes
        public static int MAX_TIMR = Integer.MAX_VALUE;

        public static long MIN_DATE = 0l; // As milliseconds
        public static long MAX_DATE = System.currentTimeMillis();

        public static int YEAR = Calendar.getInstance().get(Calendar.YEAR);

    }
}
