package com.teroki.rokego_android;

/**
 * Created by Tero on 21.9.2017.
 */

public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.teroki.rokego_android.action.main";
        public static String STARTFOREGROUND_ACTION = "com.teroki.rokego_android.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.teroki.rokego_android.action.stopforeground";
        public static String PAUSE_ACTION="com.teroki.rokego_android.action.pause";
    }

    public interface BROADCAST {
        public  static String PAUSE_BC = "com.teroki.rokego_android.PAUSE_BC";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public interface GPS_UPDATES {
        public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        public static final long MIN_TIME_BETWEEN_UPDATES = 1000 * 3 * 1; // Updating time in milliseconds (3 second)
    }

    public interface BUTTON_STATES{
        public static final int BTN_START = 1;
        public static final int BTN_PAUSE = 2;
        public static final int BTN_CONTINUE = 3;
    }

}
