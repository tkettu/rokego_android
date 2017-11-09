package com.teroki.rokego_android;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.teroki.interfaces.Constants;
import com.teroki.rokego_db.DBHelper;
import com.teroki.rokego_helpers.AssetLoader;
import com.teroki.rokego_helpers.DateHelper;
import com.teroki.rokego_helpers.JSONHelper;
import com.teroki.rokego_objects.Exercise;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

public class SaveData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button saveBtn;
    EditText eDist;
    EditText eHour, eMinutes, eSeconds;
    EditText dateText;
    String distance, time;
    int[] timeList;
    int hours, minutes, seconds;
    String name = ""; // name of sport
    String type = ""; // Type of sport (sub name)
    long date;
    Spinner sports, sportsType;
    private String LOG_TAG = "SaveData";

    Calendar mCalendar = Calendar.getInstance();

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
        timeList = DateHelper.timeToArray(time); // time (h:mm:ss or mm:ss) to [h, mm, ss]

        eHour = (EditText) findViewById(R.id.time_edit_hour);
        eHour.setText(""+timeList[0]);
        eMinutes = (EditText) findViewById(R.id.time_edit_minutes);
        eMinutes.setText(""+timeList[1]);
        eSeconds = (EditText) findViewById(R.id.time_edit_seconds);
        eSeconds.setText(""+timeList[2]);

        eDist = (EditText) findViewById(R.id.distance_edit);
        eDist.setText(distance);

        ArrayList sportList = JSONHelper.getKeyList(loadSportJson());

        sports = (Spinner) findViewById(R.id.sports_spinner);
        sportsType = (Spinner) findViewById(R.id.sports_type_spinner);

        /*ArrayAdapter<String> sports_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sportlist);*/
        ArrayAdapter<String> sports_adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, sportList);
        sports.setAdapter(sports_adapter);
        sports.setSelection(0);
        //sports.setOnItemClickListener(changeSports());
        sports.setOnItemSelectedListener(this);

        setDatePicker();


        //name = sports.getSelectedItem().toString();

    }

    private void setDatePicker() {
        dateText = (EditText) findViewById(R.id.sports_date);
        final DatePickerDialog.OnDateSetListener dateP = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateSportsDate();
            }
        };
        dateText.setText(DateHelper.getDate(System.currentTimeMillis()));
        dateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                    new DatePickerDialog(SaveData.this, dateP, mCalendar.get(Calendar.YEAR),
                            mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SaveData.this, dateP, mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateSportsDate() {
        String dFormat = Constants.DATE_FORMAT.DEFAULT_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(dFormat, Locale.getDefault());

        dateText.setText(sdf.format(mCalendar.getTime()));
    }


    public void saveDataToDB(View view){
        Log.d("Adding: ", String.valueOf(time) + " AND " + String.valueOf(distance));
        DBHelper db = new DBHelper(this);
        //Todo time = getEditetext ja Distance sama, then check if empty/null

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
        String sTime = DateHelper.concatTime(eHour.getText().toString(),
                                            eMinutes.getText().toString(),
                                            eSeconds.getText().toString()); //eTime.getText().toString();
        //TODO Should we save time as ms
        sTime = (sTime != null ? sTime : "0:0");

        //date = System.currentTimeMillis();
        String sDate = dateText.getText().toString();
        date = DateHelper.dateToMillis(sDate);

        Exercise exercise = new Exercise(name,type, sDistance, sTime, date);
        db.addExercise(exercise);

        Intent intent = new Intent(SaveData.this, Exercises.class);
        startActivity(intent);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String sName = adapterView.getItemAtPosition(i).toString();
        this.name = sName;

        ArrayList<String> st = JSONHelper.getValueList(loadSportJson(), sName);

        /*ArrayAdapter<String> stype_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, st);*/
        ArrayAdapter<String> stype_adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, st);
        sportsType.setAdapter(stype_adapter);
        sportsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String mName = adapterView.getItemAtPosition(i).toString();
                if (mName != ""){
                    Log.d(LOG_TAG, "Name is " + mName);
                    /*name = mName;*/
                    type = mName;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private String loadSportJson(){
        try {
            InputStream stream = getAssets().open(Constants.ASSET_FILES.SPORTS_FILE);
            return AssetLoader.loadSports(stream);
        } catch (IOException e) {
            Log.e(LOG_TAG, String.format("Couldn't open file {s}", Constants.ASSET_FILES.SPORTS_FILE));
            return "NO FILE";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
