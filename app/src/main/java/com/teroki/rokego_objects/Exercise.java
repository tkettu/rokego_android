package com.teroki.rokego_objects;

import com.teroki.rokego_helpers.DateHelper;

/**
 * Created by Tero on 13.9.2017.
 */

public class Exercise {

    private int _id;
    private String _name;
    private double _distance;
    private String _time;
    private long _date; //date as milliseconds

    public Exercise() {
    }

    public Exercise(int _id, String _name, double _distance, String _time, long _date) {
        this._id = _id;
        this._name = _name;
        this._distance = _distance;
        this._time = _time;
        this._date = _date;
    }

    public Exercise(String _name, double _distance, String _time, long _date) {
        this._name = _name;
        this._distance = _distance;
        this._time = _time;
        this._date = _date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public double get_distance() {
        return _distance;
    }

    public void set_distance(double _distance) {
        this._distance = _distance;
    }

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public long get_date() {
        return _date;
    }

    public void set_date(long _date) {
        this._date = _date;
    }

    public String format_date(){
        DateHelper dateHelper = new DateHelper();
        return dateHelper.getDate(get_date());
    }

    public double timeToHours(){
        String[] t = _time.split(":"); // h:min:secs to [h][min][secs]
        double tt = 0.0;
        if (t.length == 1){
            tt = Double.parseDouble(t[0])/(60 *60);
        }else if(t.length == 2){
            tt = Double.parseDouble(t[1])/(60 *60) + Double.parseDouble(t[0])/60;
        }else {
            tt = Double.parseDouble(t[2])/(60 *60) + Double.parseDouble(t[1])/60 + Double.parseDouble(t[0]);
        }
        return Math.round(tt * 1000d) / 1000d;
    }


}
