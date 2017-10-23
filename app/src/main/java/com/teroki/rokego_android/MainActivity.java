package com.teroki.rokego_android;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
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


public class MainActivity extends AppCompatActivity{

    //Permissions
    String mPermissionFL = Manifest.permission.ACCESS_FINE_LOCATION;

    private static final int REQUEST_CODE_PERMISSION = 2;

    public static final String TIME_MSG = "com.teroki.rokego_mobile.MESSAGE_TIME";
    public static final String DISTANCE_MSG= "com.teroki.rokego_mobile.MESSAGE_DISTANCE";

    Button startBtn;   //Visible on Create, pressing changes text to "pause" and starts tracking
    Button stopBtn;    //Invisible on Create, visible when pressed pause, invisible again on "continue"
    FloatingActionButton addBtn;
    private boolean startBtnClicked = false;


    Toolbar mainToolbar;

    LinearLayout timeLayout, distanceLayout;

    PausableChronometer chronometer;
    TextView distance;
    TextView searching;

    TextView timeLabel;


    GpsTracker gps;
    Tracker tracker;
    private String LIFECYCLE_TAG = "Acitivity cycle:";
    private static final String LOG_TAG = "Main Activity";

    public static boolean active = false;

    //Broadcast receiver
    MessageReceiver receiver = null;
    Boolean receiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        receiver = new MessageReceiver();

        startBtn = (Button) findViewById(R.id.button_start);
        stopBtn = (Button) findViewById(R.id.button_stop);
        mainToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        addBtn = (FloatingActionButton) findViewById(R.id.fab_Add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SaveData.class);
                startActivity(intent);
            }
        });

        timeLayout = (LinearLayout) findViewById(R.id.time_layout);
        distanceLayout = (LinearLayout) findViewById(R.id.distance_layout);

        chronometer = (PausableChronometer) findViewById(R.id.chronometer);
        distance = (TextView) findViewById(R.id.distance);
        searching = (TextView) findViewById(R.id.location);
        setSupportActionBar(mainToolbar);


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
                gps.trackingStarted = false;


            }
        }catch (Exception e){

        }

        Log.d(LOG_TAG, "Creating " + chronometer.toString() + " and " + gps.toString());
        tracker = new Tracker(this, chronometer, gps);

        // Todo, check Location permissions when implemented
       /* gps = new Tracker(MainActivity.this);
        // Todo turn of gps when closing app and tracking not started

*/
    }


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
            continue2();
            /*chronometer.start();
            gps.start(); // Todo, what if location permission not granted???
            */

            //if (gps.canGetLocation()){
            //    searching.setText("Location found");
            //}


        }else{
            pause();

        }

    }

    public void continue2(){



        //chronometer.start();
        //gps.start(); // Todo, what if location permission not granted???
        Intent startIntent = new Intent(MainActivity.this, ForeGroundService.class);


        startIntent.putExtra("elapsedTime", chronometer.getBase());
        startIntent.putExtra("elapsedDistance", gps.getDistance());
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);
        //startBtn.setText(R.string.btn_pause);
        setButtonState(Constants.BUTTON_STATES.BTN_PAUSE);

        tracker.start();

        stopBtn.setVisibility(View.INVISIBLE);

        startBtnClicked = true;
    }

    public void pause() {
        //chronometer.stop();
        //gps.pause();
        tracker.pause();
        setButtonState(Constants.BUTTON_STATES.BTN_CONTINUE);

        startBtnClicked = false;

    }

    /**
     * Stops tracking and send to summary page
     */
    public void stop(View view){


        stopForeground();

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
        resetGps();
        //gps.onDestroy();

        chronometer.reset();
        //setStartButton(false);
        setButtonState(Constants.BUTTON_STATES.BTN_START);
    }

    public void stopForeground(){
        Intent stopIntent = new Intent(MainActivity.this, ForeGroundService.class);
        stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        startService(stopIntent);
    }

    private void resetGps(){
        gps.setDistance(0.0);
        gps.stopLocationUpdates();
        gps.trackingStarted = false;
    }


    /**
     * Sets buttons and layout states (Visibility, text) between 1-START, 2-PAUSE and 3-CONTINUE/STOP
     * States should go from 1 -> 2 -> 3 -> 1
     * @param state
     */
    private void setButtonState(int state){
        switch (state){
            case Constants.BUTTON_STATES.BTN_START:
                startBtn.setText(R.string.btn_start);
                addBtn.setVisibility(View.VISIBLE);
                timeLayout.setVisibility(View.INVISIBLE);
                distanceLayout.setVisibility(View.INVISIBLE);
                break;
            case Constants.BUTTON_STATES.BTN_PAUSE:
                startBtn.setText(R.string.btn_pause);
                addBtn.setVisibility(View.INVISIBLE);
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

    /*private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "BroadcastReceiver");
            pause();
        }
    };*/

    @Override
    protected void onResume(){
        Log.d(LOG_TAG, "OnResume");
        active = true;
        if(!receiverRegistered){
            IntentFilter filter = new IntentFilter();

            Log.d(LOG_TAG, "OnResume Pause");

            filter.addAction(Tracker.PAUSE_BROADCAST);
            registerReceiver(receiver, filter);
            receiverRegistered = true;
        }

        super.onResume();
    }

    @Override
    protected void onPause(){

        active = false;
        Log.d(LOG_TAG, "MainActivity paused");

        if(receiverRegistered){
            unregisterReceiver(receiver);
            receiverRegistered = false;
        }

        if (!gps.trackingStarted){
            Log.d(LIFECYCLE_TAG, "Stopping updates");
            gps.stopLocationUpdates();
            gps.onDestroy();
            stopForeground();  // Todo this should not be even started if tracking not started

        }
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        Log.d(LIFECYCLE_TAG, "Main activity stopping");
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        // If track started, it should continue at background; otherwise close app with back pressed
        // on main page
        resetGps();
        //stopForeground();
        if (!gps.trackingStarted){
            finish();
        }
        minimizeApp();


    }

    public void minimizeApp(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(startMain);
    }

    public class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(LOG_TAG, "Message received");
            String action = intent.getAction();
            if(action.equals(Constants.BROADCAST.PAUSE_BC)){
                pause();
            }
        }
    }
}
