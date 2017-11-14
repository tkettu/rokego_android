package com.teroki.rokego_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.teroki.base_activities.BaseSelectionActivity;

public class FilterActivity extends BaseSelectionActivity {
    //http://androiddhina.blogspot.fi/2016/02/android-multi-selection-spinner.html
    //Spinner sports;
    public static final String NAME_MSG = "com.teroki.rokego_android.FILTER_NAME_MSG";
    private final String LOG_TAG = "FilterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        setSpinners();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View view, int position, long l) {
        //super.onItemSelected(adapter, view, position, l);
        this.name = adapter.getItemAtPosition(position).toString();
        /*Intent intent = new Intent(FilterActivity.this, Exercises.class);
        intent.putExtra(NAME_MSG, this.name);*/
        //startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FilterActivity.this, Exercises.class);
        intent.putExtra(NAME_MSG, this.name);
        startActivity(intent);
    }

}