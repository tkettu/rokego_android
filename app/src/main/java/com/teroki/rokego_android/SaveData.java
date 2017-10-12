package com.teroki.rokego_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teroki.rokego_db.DBHelper;
import com.teroki.rokego_objects.Exercise;

import java.util.List;

public class SaveData extends AppCompatActivity {

    Button saveBtn;
    EditText eTime, eDist;
    String distance, time;
    String name; // name of sport
    long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);

        saveBtn = (Button) findViewById(R.id.save_button);
        Intent intent = getIntent();
        //Todo check if distance is not number (Seaching location .. etc.)

        distance = intent.getStringExtra(MainActivity.DISTANCE_MSG);
        time = intent.getStringExtra(MainActivity.TIME_MSG);
        System.out.print("ROKI WAS HERE");
        Log.d("Sport: ", String.valueOf(time) + " AND " + String.valueOf(distance));

        eTime = (EditText) findViewById(R.id.time_edit);
        eTime.setText(time);
        eDist = (EditText) findViewById(R.id.distance_edit);
        eDist.setText(distance);
    }

    public void saveDataToDB(View view){
        Log.d("Adding: ", String.valueOf(time) + " AND " + String.valueOf(distance));
        DBHelper db = new DBHelper(this);
        //Todo Exercise name with choices
        // Todo date
        date = System.currentTimeMillis();
        if( distance == null || time == null){
            db.addExercise(new Exercise("Running", 0.0, "0:0", date));
        }
        else{
            double d = 0.0;
            try{
                d = Double.parseDouble(distance);
            }catch (NumberFormatException e){
                d = 0.0;
            }
            Exercise exercise = new Exercise("Running",d,time, date);

            db.addExercise(exercise);
        }


        Log.d("Exercises","All exercises..");
        List<Exercise> exercises = db.getExercises();

        for(Exercise e: exercises){
            String log = "Id: " + e.get_id() + ", Name: " + e.get_name() + ", distance: " + e.get_distance()
                    + ", time: " + e.get_time();
            Log.d("Exercise: ", log);
        }
        //Todo
        Intent intent = new Intent(SaveData.this, Exercises.class);
        startActivity(intent);

    }
}
