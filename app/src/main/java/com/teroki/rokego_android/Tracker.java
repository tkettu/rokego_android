package com.teroki.rokego_android;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.teroki.rokego_helpers.PausableChronometer;


/**
 * Service for time and distance tracking at the background
 */
public class Tracker extends Service /*implements LocationListener */{

    private static final String LOG_TAG = "Tracker";

    private final Context mContext;
    private final int ONGOING_NOTIFICATION_ID = 1;



    public static String PAUSE_BROADCAST = "com.teroki.rokego_android.PAUSE_BC";

    GpsTracker gps;
    PausableChronometer chronometer;

    private MessageReceiver receiver = null;

    ForeGroundService fgService;

    public Tracker(){
        this.mContext = null;
    }

    public Tracker(Context mContext, PausableChronometer mChronometer, GpsTracker mGps) {
        this.mContext = mContext;
        this.chronometer = mChronometer;
        this.gps = mGps;
        receiver = new MessageReceiver();
        Log.d(LOG_TAG, "Creating " + this.chronometer.toString() + " and " + this.gps.toString());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
   /*

    */

    /*@Override
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


            //start();


        }else if(intent.getAction().equals(Constants.ACTION.MAIN_ACTION)){
            //return to app
            start();
            Log.d(LOG_TAG, "Main Action");
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
    }*/


    public void pause() {
        // Todo: Implement pause/continue
        // Todo: pause functioning when mainactivity stopped (minimized)
        //Intent intent = new Intent();
        //intent.setAction()

        chronometer.stop();
        gps.pause();

        //Intent intent = new Intent();
        //intent.setAction(Constants.BROADCAST.PAUSE_BC);
        //sendBroadcast(intent);
       /* Intent intent = new Intent();

        //intent.setAction(Intent.ACTION_MAIN);
        //intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Constants.BROADCAST.PAUSE_BC);
        sendBroadcast(intent);

        //startActivity(intent);*/


        Log.d(LOG_TAG, "pause");

    }

    public void start(){
        /*Intent intent = new Intent(this, MainActivity.class);

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);*/

        Log.d(LOG_TAG, "Track started");
        chronometer.start();
        gps.start();


       /* Intent startIntent = new Intent(Tracker.this, ForeGroundService.class);
        startIntent.putExtra("elapsedTime", chronometer.getBase());
        startIntent.putExtra("elapsedDistance", gps.getDistance());
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);*/



    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    //public static class ForeGroundService extends Service{


        /*private long elapsedTime;
        private double elapsedDistance;


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


                //start();


            }else if(intent.getAction().equals(Constants.ACTION.MAIN_ACTION)){
                //return to app
                //start();
                Log.d(LOG_TAG, "Main Action");
            }
            else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)){
                pause_fg();
                Log.i(LOG_TAG, "Pressed pause");
            }else if (intent.getAction().equals(
                    Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            }
            return START_STICKY;
        }
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        void pause_fg(){
            Intent intent = new Intent();
            intent.setAction(Constants.BROADCAST.PAUSE_BC);
            sendBroadcast(intent);
        }
    }*/



    @Override
    public void onDestroy(){
        Log.d(LOG_TAG, "Tracker destroyed");
        super.onDestroy();
    }

    public class MessageReceiver extends BroadcastReceiver {

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
