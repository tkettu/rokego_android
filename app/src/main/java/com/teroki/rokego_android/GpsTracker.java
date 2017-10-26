package com.teroki.rokego_android;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.security.Provider;

public class GpsTracker extends Service implements LocationListener{

    private Context mContext;

    private TextView distanceField;
    //private TextView searchingField;
    private TextView locationField;



    private LocationManager locationManager;
    private String provider;

    Location location;
    private Location oldLocation;
    double latitude;
    double longitude;

    boolean isGpsEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    private String LOG_TAG = "GPS Tracker";



    private double distance = 0.0;

    // Distance tracking on/off
    private boolean distanceOn = false;

    public boolean trackingStarted = false;

    public GpsTracker() {
    }

    public GpsTracker(Context mContext){
        this.trackingStarted = false;
        this.mContext = mContext;
        getLocation();
    }
    
    public void start(){
        /*locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        // Note: Check permissions at the callable activity
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null){
            Log.i("Provider", "Provider " + provider + " has been selected");
            locationManager.requestLocationUpdates(provider,
                    Constants.GPS_UPDATES.MIN_TIME_BETWEEN_UPDATES,
                    Constants.GPS_UPDATES.MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this);
            oldLocation = location;
            distanceOn = true;
            searchingField.setVisibility(View.INVISIBLE);
            onLocationChanged(location);
        }else{
            Log.i("Location", "Location not available or searching...");
            //distanceField.setText("Searching location...");
            searchingField.setVisibility(View.VISIBLE);
        }
        //getLocation(location);
        */
        //getLocation();
        //Todo Check if can get location
        Location location1 = getLocation();
        this.trackingStarted = true;
        if (location1 != null) {
            oldLocation = location1;
            distanceOn = true;
            onLocationChanged(location1);
            //searchingField.setText("FOUND SOME");
            Log.d("Location", "Found");
        }else{
            //searchingField.setVisibility(View.VISIBLE);
            //searchingField.setText("STILL LOOKING GPS");
            Log.d("Location", "Not found");
        }
        //searchingField.setVisibility(View.INVISIBLE);


    }

    private Location getLocation(){
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d("Gps Enabled", (isGpsEnabled ? "Yes": "No"));
            Log.d("Network Enabled", (isNetworkEnabled ? "Yes": "No"));
            if (!isGpsEnabled && !isNetworkEnabled ){
                //Nothing enabled
            } else{
                this.canGetLocation = true;
                // Location from network

                if (isGpsEnabled) {
                    Log.d("GPS Enabled", "GPS Enabled");


                    //if (location == null) {

                    //}

                        if (locationManager != null) {
                            locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                Constants.GPS_UPDATES.MIN_TIME_BETWEEN_UPDATES,
                                Constants.GPS_UPDATES.MIN_DISTANCE_CHANGE_FOR_UPDATES,
                                this);
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Log.d("Location", location.toString() + " Tassa");

                            if (location != null) {
                                //oldLocation = location;
                                //distanceOn = true;
                                //searchingField.setVisibility(View.INVISIBLE);
                                //onLocationChanged(location);
                                locationField.setText(location.toString());

                            }else{
                                Log.i("Location", "Location not available or searching...");
                                //distanceField.setText("Searching location...");
                                //searchingField.setVisibility(View.VISIBLE);
                                locationField.setText("Location not found yet");
                            }
                        }
                    }else if (isNetworkEnabled){
                    Log.d("Network Enabled", "Network Enabled");
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            Constants.GPS_UPDATES.MIN_TIME_BETWEEN_UPDATES,
                            Constants.GPS_UPDATES.MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            this);

                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        /*if (location != null) {

                        }*/
                    }
                }
                }
            //}

        }  catch (Exception e) {
            e.printStackTrace();
        }
        return location;

    }
    private void getLocation(Location location) {


    }

    public void pause() {
        this.distanceOn = false;
    }

    @Override
    public void onDestroy() {
        //locationManager.removeUpdates();
        super.onDestroy();
        stopLocationUpdates();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onLocationChanged(Location location) {
        increaseDistance(location, oldLocation);

    }

    private void increaseDistance(Location location, Location oldLocation) {
        if (distanceOn){
            if (oldLocation != location){
                distance += location.distanceTo(oldLocation);
                Log.d(LOG_TAG, "Distance bw " + String.valueOf(location.distanceTo(oldLocation)));
            }
            // Update "global" oldLocation
            this.oldLocation = location;
            Log.d("Old location", this.oldLocation.toString());
            Log.d("Location", location.toString());
            double rDistance = (double)Math.round((distance/1000) * 100d ) / 100d; //distance as 2 decimal kms
            //Todo Check if this works
            distanceField.setText(String.valueOf(rDistance));
        }
    }

    /**
     * Stops using gps on device
     */
    public void stopLocationUpdates(){
        if (locationManager != null){
            locationManager.removeUpdates(GpsTracker.this);
            Log.d("Removing updates", "Gps closed");
        }else{
            Log.d("Removing updates", "Location manager Null");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    /**
     * Function to get latitude
     * */

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public TextView getDistanceField() {
        return distanceField;
    }

    public void setDistanceField(TextView distanceField) {
        this.distanceField = distanceField;
    }

   /* public void setSearchingField(TextView searchingField){
        this.searchingField = searchingField;
    }*/

    public void setLocationField(TextView locationField) {
        this.locationField = locationField;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


}
