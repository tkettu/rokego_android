package com.teroki.rokego_android;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import android.support.v4.app.NotificationCompat;
import android.util.Log;



/**
 * Service for time and distance tracking at the background
 */
public class Tracker extends Service /*implements LocationListener */{

    private static final String LOG_TAG = "Tracker";

    //private final Context mContext;
    private final int ONGOING_NOTIFICATION_ID = 1;

    private long elapsedTime;
    private double elapsedDistance;

    public static String PAUSE_BROADCAST = "com.teroki.rokego_android.PAUSE_BC";

    public Tracker(){

    }

    /*public Tracker(Context mContext) {
        this.mContext = mContext;
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){

            //createListener();

            Log.i(LOG_TAG, "Received foreground Intent ");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
           // notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent pauseIntent = new Intent(this, Tracker.class);
            pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
            PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0);


            //Todo Change icon
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.settings_icon);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(getText(R.string.notification_title))
                    .setTicker(getText(R.string.ticker_text))
                    .setContentText(String.valueOf(elapsedTime))
                    //.setContentText(getText(R.string.notification_message))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(icon)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(android.R.drawable.ic_media_pause, "Pause", pendingPauseIntent)
                    .build();

            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

            elapsedTime = intent.getExtras().getLong("elapsedTime");
            elapsedDistance = intent.getExtras().getDouble("elapsedDistance");


            start();


        }else if(intent.getAction().equals(Constants.ACTION.MAIN_ACTION)){
            //return to app
            Log.d("Returning", "to app");
        }
        else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)){
            pause();
            Log.i(LOG_TAG, "Pressed pause");
        }else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }


    private void pause() {
        // Todo: Implement pause/continue
        // Todo: pause functioning when mainactivity stopped (minimized)
        //Intent intent = new Intent();
        //intent.setAction()


        Intent intent = new Intent();
        intent.setAction(PAUSE_BROADCAST);
        sendBroadcast(intent);
        Log.d(LOG_TAG, "pause");

    }

    public void start(){

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
