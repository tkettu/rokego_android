package com.teroki.rokego_helpers;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Tero on 9.11.2017.
 */

public class AssetLoader {

    private static final String LOG_TAG =  "AssetLoader";

    public static String loadSports(InputStream stream){
        String contents = "";

        try {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            contents = new String(buffer);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Couldnt find " + stream.toString(), e.getCause());
        }
        return contents;

    }
}
