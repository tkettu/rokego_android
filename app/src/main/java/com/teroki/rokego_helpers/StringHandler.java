package com.teroki.rokego_helpers;

import android.util.Log;

import com.teroki.rokego_objects.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tero on 1.11.2017.
 */

public class StringHandler {

    private static final int NAME_LEN = 25;
    private static final int DIST_LEN = 10;
    private static final int TIME_LEN = 12;
    private static final int DATE_LEN = 12;

    private static String LOG_TAG = "StringHandler";

    public static List<String> fixedLengthArray(List<Exercise> strList){
       List<String> sExes = new ArrayList<>();

        for (Exercise e: strList){
            String s = fixedLengthString(e.getName().trim(), NAME_LEN, false) +  fixedLengthString(e.getTime().trim(), TIME_LEN, true)
                    + fixedLengthString(String.valueOf(e.getDistance()).trim(), DIST_LEN, true)
                    + fixedLengthString(e.formatDate().trim(), NAME_LEN, true);
            sExes.add(s);
            Log.d(LOG_TAG, s);
        }

        return sExes;
    }

    private static String fixedLengthString(String str, int len, boolean reverse){
        //return String.format("%1$" + len + "s ", str);
        return (reverse ? String.format("%1$" + len + "s", str) : String.format("%-" + len +"s", str));
        //return (reverse ? String.format("%" + len/2 + "%1$" + len/2 + "s", str)
          //                  : String.format("%-" + len +"s", str));

    }
}
