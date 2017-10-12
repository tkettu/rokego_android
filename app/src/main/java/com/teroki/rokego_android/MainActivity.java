package com.teroki.rokego_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teroki.rokego_helpers.NumericChecker;
import com.teroki.rokego_helpers.PausableChronometer;


public class MainActivity extends AppCompatActivity {

    //Permissions
    String mPermissionFL = Manifest.permission.ACCESS_FINE_LOCATION;

    private static final int REQUEST_CODE_PERMISSION = 2;

    public static final String TIME_MSG = "com.teroki.rokego_mobile.MESSAGE_TIME";
    public static final String DISTANCE_MSG= "com.teroki.rokego_mobile.MESSAGE_DISTANCE";

    Button startBtn;   //Visible on Create, pressing changes text to "pause" and starts tracking
    Button stopBtn;    //Invisible on Create, visible when pressed pause, invisible again on "continue"
    private boolean startBtnClicked = false;


    Toolbar mainToolbar;

    LinearLayout timeLayout, distanceLayout;

    PausableChronometer chronometer;
    TextView distance;
    TextView searching;

    TextView timeLabel;

    //Tracker gps;
    GpsTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = (Button) findViewById(R.id.button_start);
        stopBtn = (Button) findViewById(R.id.button_stop);
        mainToolbar = (Toolbar) findViewById(R.id.toolbar_main);

        timeLayout = (LinearLayout) findViewById(R.id.time_layout);
        distanceLayout = (LinearLayout) findViewById(R.id.distance_layout);

        chronometer = (PausableChronometer) findViewById(R.id.chronometer);
        distance = (TextView) findViewById(R.id.distance);
        searching = (TextView) findViewById(R.id.location);
        setSupportActionBar(mainToolbar);

        // Todo set state oncreate
        //setStateToStart();
        setButtonState(Constants.BUTTON_STATES.BTN_START);
        try{
            if (ActivityCompat.checkSelfPermission(this, mPermissionFL)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{mPermissionFL}, REQUEST_CODE_PERMISSION );
            } else{
                Log.d("GPS", "Enabling gps");
                gps = new GpsTracker(MainActivity.this);
                if (gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), "Sijaintisi on - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    gps.showSettingsAlert();
                }

                gps.setDistanceField((TextView) findViewById(R.id.distance));
                gps.setLocationField((TextView) findViewById(R.id.location));
            }
        }catch (Exception e){

        }

        // Todo, check Location permissions when implemented
       /* gps = new Tracker(MainActivity.this);

        gps.setChronometer((PausableChronometer) findViewById(R.id.chronometer));
        gps.setTimeLabel((TextView) findViewById(R.id.time_label));
*/
    }

   /* private void setStateToStart() {
        stopBtn.setVisibility(View.INVISIBLE);
        set
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch ( item.getItemId()){
            case R.id.action_sign_out:
                test();
                break;
            case R.id.action_exercises:
                exercises();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    private void exercises() {
        Intent intent = new Intent(MainActivity.this, Exercises.class);
        startActivity(intent);
    }

    /*@Override
    protected void onPostResume() {
        super.onPostResume();
        timeLayout.setVisibility(View.VISIBLE);
        mainToolbar.setVisibility(View.INVISIBLE);
    }*/

    public void start(View view){

        if (!startBtnClicked) {
            chronometer.start();
            gps.start(); // Todo, what if location permission not granted???
            Intent startIntent = new Intent(MainActivity.this, Tracker.class);
            startIntent.putExtra("elapsedTime", chronometer.getBase());
            startIntent.putExtra("elapsedDistance", gps.getDistance());
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
            //startBtn.setText(R.string.btn_pause);
            setButtonState(Constants.BUTTON_STATES.BTN_PAUSE);

            stopBtn.setVisibility(View.INVISIBLE);

            if (gps.canGetLocation()){
                searching.setText("Location found");
            }


        }else{
            chronometer.stop();
            gps.pause();
            //Intent stopIntent = new Intent(MainActivity.this, Tracker.class);
            //stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            //startService(stopIntent);
            //stopBtn.setVisibility(View.VISIBLE);
            //startBtn.setText(R.string.btn_continue);
            setButtonState(Constants.BUTTON_STATES.BTN_CONTINUE);
        }
        startBtnClicked = !startBtnClicked;
       /* //gps.getTimeLabel().setVisibility(View.VISIBLE);
        //gps.getChronometer().setVisibility(View.VISIBLE);
        gps.start();
        this.mainToolbar.setVisibility(View.INVISIBLE);
*/
    }

    /**
     * Stops tracking and send to summary page
     */
    public void stop(View view){

        Intent stopIntent = new Intent(MainActivity.this, Tracker.class);
        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        startService(stopIntent);


        Intent saveIntent = new Intent(MainActivity.this, SaveData.class);
        TextView tDistance = (TextView) findViewById(R.id.distance);
        PausableChronometer tTime = (PausableChronometer) findViewById(R.id.chronometer);

        String mDistance = tDistance.getText().toString();
        NumericChecker nChecker = new NumericChecker();
        mDistance = (nChecker.isNumeric(mDistance) ? mDistance: "0.0" );

        String mTime = tTime.getText().toString();
        saveIntent.putExtra(TIME_MSG, mTime);
        saveIntent.putExtra(DISTANCE_MSG, mDistance);
        startActivity(saveIntent);

        //reset distance
        gps.setDistance(0.0);
        gps.onDestroy();

        chronometer.reset();
        //setStartButton(false);
        setButtonState(Constants.BUTTON_STATES.BTN_START);
    }

    /*private void setStartButton(boolean b) {

        startBtnClicked = b;
        startBtn.setText((b ? R.string.btn_pause : R.string.btn_start));
        stopBtn.setVisibility((b ? View.INVISIBLE: View.VISIBLE));
    }*/

    /**
     * Sets start and stop button states between 1-START, 2-PAUSE and 3-CONTINUE/STOP
     * @param state
     */
    private void setButtonState(int state){
        switch (state){
            case Constants.BUTTON_STATES.BTN_START:
                startBtn.setText(R.string.btn_start);
                timeLayout.setVisibility(View.INVISIBLE);
                distanceLayout.setVisibility(View.INVISIBLE);
                break;
            case Constants.BUTTON_STATES.BTN_PAUSE:
                startBtn.setText(R.string.btn_pause);
                timeLayout.setVisibility(View.VISIBLE);
                distanceLayout.setVisibility(View.VISIBLE);
                break;
            case Constants.BUTTON_STATES.BTN_CONTINUE:
                startBtn.setText(R.string.btn_continue);
                break;
            default:
                startBtn.setText(R.string.btn_start);
                break;
        }
        stopBtn.setVisibility((
                state == Constants.BUTTON_STATES.BTN_CONTINUE ? View.VISIBLE: View.INVISIBLE));
    }



    public void test(){
        //Intent intent = new Intent(this, TestActivity.class);
        //startActivity(intent);
        Intent stopIntent = new Intent(MainActivity.this, Tracker.class);
        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        startService(stopIntent);
        startBtn.setText("LOPETETTU JOO");
    }
}
