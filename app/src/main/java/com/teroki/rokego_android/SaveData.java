package com.teroki.rokego_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.teroki.rokego_db.DBHelper;
import com.teroki.rokego_objects.Exercise;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SaveData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button saveBtn;
    EditText eTime, eDist;
    String distance, time;
    String name = ""; // name of sport
    long date;
    Spinner sports, sportsType;
    private String LOG_TAG = "SaveData";

    JSONObject jsonObject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_data);

        saveBtn = (Button) findViewById(R.id.save_button);
        Intent intent = getIntent();
        //Todo check if distance is not number (Seaching location .. etc.)

        distance = intent.getStringExtra(MainActivity.DISTANCE_MSG);
        if (distance ==""){
            distance = "0,0";
        }
        time = intent.getStringExtra(MainActivity.TIME_MSG);
        if (time == ""){
            time = "0:0";
        }
        Log.d("Sport: ", String.valueOf(time) + " AND " + String.valueOf(distance));

        eTime = (EditText) findViewById(R.id.time_edit);
        eTime.setText(time);
        eDist = (EditText) findViewById(R.id.distance_edit);
        eDist.setText(distance);

        ArrayList sportlist = getSportList("sports.json");

        sports = (Spinner) findViewById(R.id.sports_spinner);
        sportsType = (Spinner) findViewById(R.id.sports_type_spinner);

        ArrayAdapter<String> sports_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sportlist);

        sports.setAdapter(sports_adapter);
        sports.setSelection(0);
        //sports.setOnItemClickListener(changeSports());
        sports.setOnItemSelectedListener(this);

        //name = sports.getSelectedItem().toString();

    }



    public void saveDataToDB(View view){
        Log.d("Adding: ", String.valueOf(time) + " AND " + String.valueOf(distance));
        DBHelper db = new DBHelper(this);
        //Todo time = getEditetext ja Distance sama, then check if empty/null
        // Todo date

        if (name != ""){

        }else{
            name = "Running";
        }

        double sDistance;
        try {
             sDistance = Double.parseDouble(eDist.getText().toString());
        }catch (NumberFormatException ne){
            sDistance = 0.0;
        }
        String sTime = eTime.getText().toString();
        sTime = (sTime != null ? sTime : "0:0");

        date = System.currentTimeMillis();

        Exercise exercise = new Exercise(name, sDistance, sTime, date);
        db.addExercise(exercise);

       /* if( distance == null || time == null){
            db.addExercise(new Exercise(name, 0.0, "0:0", date));
        }
        else{
            double d = 0.0;
            try{
                d = Double.parseDouble(distance);
            }catch (NumberFormatException e){
                d = 0.0;
            }
            Exercise exercise = new Exercise(name,d,time, date);

            db.addExercise(exercise);
        }
*/

        Log.d("Exercises","All exercises..");
        List<Exercise> exercises = db.getExercises();

        for(Exercise e: exercises){
            String log = "Id: " + e.get_id() + ", Name: " + e.get_name() + ", distance: " + e.get_distance()
                    + ", time: " + e.get_time();
            Log.d("Exercise: ", log);
        }

        Intent intent = new Intent(SaveData.this, Exercises.class);
        startActivity(intent);

    }

    // Todo load sports types to sport_type spinner by main sport
    private ArrayList<String> getSportList(String filename){
        String jo = loadSports("sports.json");


        try {
            jsonObject = new JSONObject(jo);
        }catch (JSONException je){
            Log.e(LOG_TAG, "Json error", je.getCause());
        }

        Iterator<?> keys = jsonObject.keys();
        ArrayList<String> mainSports = new ArrayList<>();
        try{
            while (keys.hasNext()){
                String key = (String)keys.next();
                Log.d(LOG_TAG, key );
                mainSports.add(key);
                Log.d(LOG_TAG, jsonObject.getString(key));
                if (jsonObject.get(key) instanceof  JSONObject){

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mainSports;
    }

    // Load sports.json from assets
    private String loadSports(String filename){
        String contents = "";
        try {
            InputStream stream = getAssets().open(filename);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            contents = new String(buffer);


        }catch (IOException e){
            Log.e(LOG_TAG, "Couldnt find " + filename, e.getCause());
        }
        return contents;
    }

    private ArrayList<String> getSportsType(String name){
        String jo = loadSports("sports.json");
        ArrayList<String> sportsTypes = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(jo);

             sportsTypes = new ArrayList<>();
            String s = jsonObject.getString(name);
            String[] types = s.split(",");
            for (String si : types){
                String sir = si.replace("[","").replace("]","").replace("\"","");
                sportsTypes.add(sir);
            }
            sportsTypes.add(0, name);
            //sportsTypes = (ArrayList<String>) jsonObject.getString(name).split(",");

        }catch (JSONException je){
            je.printStackTrace();
        }


        return sportsTypes;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String sName = adapterView.getItemAtPosition(i).toString();
        this.name = sName;
        try {
            Log.d(LOG_TAG, jsonObject.getString(sName));
            ArrayList<String> st = getSportsType(sName);

            ArrayAdapter<String> stype_adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, st);
            sportsType.setAdapter(stype_adapter);
            sportsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String mName = adapterView.getItemAtPosition(i).toString();
                    if (mName != ""){
                        Log.d(LOG_TAG, "Name is " + mName);
                        name = mName;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
