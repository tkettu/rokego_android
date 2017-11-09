package com.teroki.rokego_objects;

import com.teroki.rokego_helpers.DateHelper;

/**
 * Created by Tero on 13.9.2017.
 */

public class Exercise {

    private long _id;
    private String _name;
    private String _type;
    private double _distance;
    private String _time;
    private long _date; //date as milliseconds

    public Exercise() {
    }

    public Exercise(int _id, String _name, double _distance, String _time, long _date) {
        this._id = _id;
        this._name = _name;
        this._type = "";
        this._distance = _distance;
        this._time = _time;
        this._date = _date;
    }

    public Exercise(String _name, double _distance, String _time, long _date) {
        this._name = _name;
        this._type = "";
        this._distance = _distance;
        this._time = _time;
        this._date = _date;
    }

    public Exercise(int _id, String _name, String _type, double _distance, String _time, long _date) {
        this._id = _id;
        this._name = _name;
        this._type = _type;
        this._distance = _distance;
        this._time = _time;
        this._date = _date;
    }

    public Exercise(String _name, String _type, double _distance, String _time, long _date) {
        this._name = _name;
        this._type = _type;
        this._distance = _distance;
        this._time = _time;
        this._date = _date;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public double getDistance() {
        return _distance;
    }

    public void setDistance(double _distance) {
        this._distance = _distance;
    }

    public String getTime() {
        return _time;
    }

    public void setTime(String _time) {
        this._time = _time;
    }

    public long getDate() {
        return _date;
    }

    public void setDate(long _date) {
        this._date = _date;
    }

    public String getType() {
        return _type;
    }

    public void setType(String _type) {
        this._type = _type;
    }

    public String formatDate(){

        return DateHelper.getDate(getDate());
    }

    public double timeToHours(){
        String[] t = _time.split(":"); // h:min:secs to [h][min][secs]
        double tt;
        if (t.length == 1){
            tt = Double.parseDouble(t[0])/(60 *60);
        }else if(t.length == 2){
            tt = Double.parseDouble(t[1])/(60 *60) + Double.parseDouble(t[0])/60;
        }else {
            tt = Double.parseDouble(t[2])/(60 *60) + Double.parseDouble(t[1])/60 + Double.parseDouble(t[0]);
        }
        return Math.round(tt * 1000d) / 1000d;
    }

    @Override
    public String toString(){
        //Todo Formatting
        return (_name + " " + _time + " " + String.valueOf(_distance) + "\n " + _type + " "  + formatDate());
    }


}
