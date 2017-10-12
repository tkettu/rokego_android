package com.teroki.rokego_android;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

import com.teroki.rokego_helpers.PausableChronometer;


/**
 * Service for time and distance tracking at the background
 */
public class Tracker extends Service /*implements LocationListener */{

    private static final String LOG_TAG = "Tracker";

    //private final Context mContext;
    private final int ONGOING_NOTIFICATION_ID = 1;

    private long elapsedTime;
    private double elapsedDistance;



    //private PausableChronometer chronometer;
    //private TextView timeLabel;

    public Tracker(){
       // mContext = null;
    }

    /*public Tracker(Context mContext) {
        this.mContext = mContext;
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){
            Log.i(LOG_TAG, "Received foreground Intent ");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Todo Change icon
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.settings_icon);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getText(R.string.notification_title))
                    .setTicker(getText(R.string.ticker_text))
                    .setContentText(getText(R.string.notification_message))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();

            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

            elapsedTime = intent.getExtras().getLong("elapsedTime");
            elapsedDistance = intent.getExtras().getDouble("elapsedDistance");


            start();


        }else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    public void start(){



        /*Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
                .setSmallIcon(R.drawable.settings_icon)
                .setContentIntent(pendingIntent)
                .setTicker(getText(R.string.ticker_text))
                .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);*/
        // Todo StartForeground



    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

   /* @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }*/

}
