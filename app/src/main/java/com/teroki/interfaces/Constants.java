package com.teroki.interfaces;

/**
 * Created by Tero on 21.9.2017.
 */

public class Constants {
    public interface ACTION {
        String MAIN_ACTION = "com.teroki.rokego_android.action.main";
        String STARTFOREGROUND_ACTION = "com.teroki.rokego_android.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.teroki.rokego_android.action.stopforeground";
        String PAUSE_ACTION="com.teroki.rokego_android.action.pause";
    }

    public interface BROADCAST {
        String PAUSE_BC = "com.teroki.rokego_android.PAUSE_BC";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

    public interface GPS_UPDATES {
        long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        long MIN_TIME_BETWEEN_UPDATES = 1000 * 3 * 1; // Updating time in milliseconds (3 second)
    }

    public interface BUTTON_STATES{
        int BTN_START = 1;
        int BTN_PAUSE = 2;
        int BTN_CONTINUE = 3;
    }

    public interface DATE_FORMAT{
        String DEFAULT_FORMAT = "dd/MM/yyyy";
    }

    public interface ASSET_FILES{
        String SPORTS_FILE = "sports.json";
    }

}
