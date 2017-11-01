package com.teroki.rokego_helpers;

import android.util.Log;

import com.teroki.rokego_objects.Exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tero on 1.11.2017.
 */

public class StringHandler {

    private final int NAME_LEN = 25;
    private final int DIST_LEN = 10;
    private final int TIME_LEN = 12;
    private final int DATE_LEN = 12;

    String LOG_TAG = "StringHandler";

    public List<String> fixedLengthArray(List<Exercise> strList){
       List<String> sExes = new ArrayList<>();

        for (Exercise e: strList){
            String s = fixedLengthString(e.get_name().trim(), NAME_LEN, false) +  fixedLengthString(e.get_time().trim(), TIME_LEN, true)
                    + fixedLengthString(String.valueOf(e.get_distance()).trim(), DIST_LEN, true)
                    + fixedLengthString(e.format_date().trim(), NAME_LEN, true);
            sExes.add(s);
            Log.d(LOG_TAG, s);
        }

        return sExes;
    }

    private String fixedLengthString(String str, int len, boolean reverse){
        //return String.format("%1$" + len + "s ", str);
        return (reverse ? String.format("%1$" + len + "s", str) : String.format("%-" + len +"s", str));
        //return (reverse ? String.format("%" + len/2 + "%1$" + len/2 + "s", str)
          //                  : String.format("%-" + len +"s", str));

    }
}
