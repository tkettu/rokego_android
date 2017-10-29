package com.teroki.rokego_android;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Settings;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teroki.rokego_db.DBHelper;
import com.teroki.rokego_helpers.DateHelper;
import com.teroki.rokego_objects.Exercise;

import java.util.List;

public class Exercises extends Activity /* extends ListActivity */{

    private ListView exercises;
    private TextView totalTime;
    private TextView totalDistance;
    //private DBHelper db;

    private DateHelper dateHelper;

    //Todo totalDistance and time layouts and exerciseList to table?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        dateHelper = new DateHelper();

        totalTime = findViewById(R.id.textView_totalTime);
        totalDistance = findViewById(R.id.textView_totalDistance);
        exercises = findViewById(R.id.list);

        DBHelper db = new DBHelper(this);

        //Log.d("Harjoitusksia ", String.valueOf( db.countExercises()));

        //List<Exercise> exes =  db.getExercises();
       /* String[] exs;
        for (Exercise e: exes){

        }*/
        List<String> exes = db.exerciseList();

        ListAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                exes);

        exercises.setAdapter(adapter);

        List<Exercise> exesE = db.getExercises();

        setTotalTime(exesE);
        setTotalDistance(exesE);

        //exercises = (ListView) findViewById();
        //exercises.setAdapter();
    }

    private void setTotalTime(List<Exercise> exesE) {

        double tt = 0.0;

        for (Exercise e: exesE){
            double ut = e.timeToHours();
            tt += ut;
        }

        totalTime.setText("Time: " + dateHelper.hoursToTime(tt) );

    }

    private void setTotalDistance(List<Exercise> exesE){
        double td = 0.0;

        for (Exercise e: exesE){
            double ud = e.get_distance();
            td += ud;
        }

        totalDistance.setText(String.valueOf(Math.round(td * 1000d) / 1000d) + " km.");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Exercises.this, MainActivity.class);
        startActivity(intent);
        //super.onBackPressed();
    }

    /* @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }*/
}
