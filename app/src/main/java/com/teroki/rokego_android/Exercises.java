package com.teroki.rokego_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.teroki.interfaces.Filters;
import com.teroki.rokego_db.DBHelper;
import com.teroki.rokego_helpers.DateHelper;
import com.teroki.rokego_helpers.StringHandler;
import com.teroki.rokego_objects.Exercise;

import java.util.List;

public class Exercises extends Activity /* extends ListActivity */{

    private ListView exercises;
    private TextView totalTime;
    private TextView totalDistance;
    private DBHelper db;

    private String LOG_TAG = "Exercises";

    private String name = "";

    public static final String EXERCISE_MSG = "com.teroki.rokego_android.EDIT_EXERCISE";

    //Todo totalDistance and time layouts and exerciseList to table?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        totalTime = findViewById(R.id.textView_totalTime);
        totalDistance = findViewById(R.id.textView_totalDistance);
        exercises = findViewById(R.id.list);

         db = new DBHelper(this);

        //Log.d("Harjoitusksia ", String.valueOf( db.countExercises()));

        //List<Exercise> exes =  db.getExercises();
       /* String[] exs;
        for (Exercise e: exes){

        }*/
        //List<Exercise> exesE = db.getExercises();
        Intent intent = getIntent();
        List<Exercise> exesE;
        if (intent.getStringExtra(FilterActivity.NAME_MSG) != null){
            name = intent.getStringExtra(FilterActivity.NAME_MSG);
            String[] names = {name};
            exesE = db.getExercises(names, null, null,
                    Filters.EXERCISE_FILTERS.MIN_DISTANCE, Filters.EXERCISE_FILTERS.MAX_DISTANCE,
                    null,null);
        }else{
            exesE = db.getExercises();
        }
        /*String[] sports = {"Running", "Walking"};
        List<Exercise> exesE = db.getExercises(sports, null, null,
                Filters.EXERCISE_FILTERS.MIN_DISTANCE, Filters.EXERCISE_FILTERS.MAX_DISTANCE,
                null,null);*/

        setTotalTime(exesE);
        setTotalDistance(exesE);
        makeExerciseList(exesE);


        //exercises = (ListView) findViewById();
        //exercises.setAdapter();
    }

    private void makeExerciseList(List<Exercise> exesE) {
        List<String> exes = StringHandler.fixedLengthArray(exesE);//db.exerciseList();

        /*final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                exes);*/
        ListAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                exesE);

        exercises.setAdapter(adapter);

        exercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                /*Exercise ex = (Exercise)adapter.getItemAtPosition(position);
                Intent intent = new Intent(Exercises.this, EditExercise.class);
                intent.putExtra(EXERCISE_MSG, ex.getId());
                startActivity(intent);*/

                // Test
                Intent intent = new Intent(Exercises.this, FilterActivity.class);
                startActivity(intent);

            }
        });
    }


    private void setTotalTime(List<Exercise> exesE) {

        double tt = 0.0;

        for (Exercise e: exesE){
            double ut = e.timeToHours();
            tt += ut;
        }

        totalTime.setText(DateHelper.hoursToTime(tt) );

    }

    private void setTotalDistance(List<Exercise> exesE){
        double td = 0.0;

        for (Exercise e: exesE){
            double ud = e.getDistance();
            td += ud;
        }

        totalDistance.setText(String.valueOf(Math.round(td * 1000d) / 1000d));
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

    /*private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }*/
}
