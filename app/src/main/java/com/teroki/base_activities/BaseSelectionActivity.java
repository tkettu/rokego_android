package com.teroki.base_activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.teroki.interfaces.Constants;
import com.teroki.rokego_android.R;
import com.teroki.rokego_helpers.AssetLoader;
import com.teroki.rokego_helpers.JSONHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class BaseSelectionActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    private Spinner sports, sportsType;
    public String name = ""; // name of sport
    public String type = ""; // Type of sport (sub name)
    private String LOG_TAG = "BaseSelectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_selection);

        setSpinners();

    }



    public void setSpinners() {

        sports =(Spinner)findViewById(R.id.base_sports_spinner);
        sportsType = (Spinner)findViewById(R.id.base_sports_type_spinner);

        ArrayList sportList = JSONHelper.getKeyList(loadSportJson());

        ArrayAdapter<String> sports_adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, sportList);
        sports.setAdapter(sports_adapter);
        sports.setSelection(0);
        //sports.setOnItemClickListener(changeSports());
        sports.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {

        String sName = adapter.getItemAtPosition(position).toString();
        this.name = sName;

        ArrayList<String> st = JSONHelper.getValueList(loadSportJson(), sName);

        ArrayAdapter<String> stypeAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, st);
        sportsType.setAdapter(stypeAdapter);
        sportsType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {
                String mName = adapter.getItemAtPosition(position).toString();
                if (mName != ""){
                    Log.d(LOG_TAG, "Name is " + mName);

                    type = mName;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
}
