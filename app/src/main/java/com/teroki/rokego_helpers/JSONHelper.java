package com.teroki.rokego_helpers;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Tero on 9.11.2017.
 */

public class JSONHelper {

    private static String LOG_TAG = "JSONHelper";
    private static JSONObject jsonObject = null;

    /**
     * Get keys (sportlist) from JSON file json
     * @param json JSON file as String
     * @return keys from json (main Sports)
     */

    public static ArrayList<String> getKeyList(String json){
        try {
            jsonObject = new JSONObject(json);
        }catch (JSONException je){
            Log.e(LOG_TAG, "Json error", je.getCause());
        }

        Iterator<?> keys = jsonObject.keys();
        ArrayList<String> keyList = new ArrayList<>();
        try{
            while (keys.hasNext()){
                String key = (String)keys.next();
                Log.d(LOG_TAG, key );
                keyList.add(key);
                Log.d(LOG_TAG, jsonObject.getString(key));
                /*if (jsonObject.get(key) instanceof  JSONObject){
                //Why this is here???
                }*/
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keyList;
    }

    /**
     * Return values of Json by key name
     * <p>
     *     Json type {
     *               key1: ["",value11, value12,...],
     *               key2: ["",value21, value22,...],
     *               ...
     *               }
     * </p>
     * @param json JSON file as String
     * @param name Key name
     * @return Values corresponding to key
     */
    public static ArrayList<String> getValueList(String json, String name){

        ArrayList<String> valueList = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(json);

            valueList = new ArrayList<>();
            String s = jsonObject.getString(name);
            String[] types = s.split(",");
            for (String si : types){
                String sir = si.replace("[","").replace("]","").replace("\"","");
                valueList.add(sir);
            }
            valueList.add(0, name);
            //sportsTypes = (ArrayList<String>) jsonObject.getString(name).split(",");

        }catch (JSONException je){
            je.printStackTrace();
        }

        return valueList;
    }

}
