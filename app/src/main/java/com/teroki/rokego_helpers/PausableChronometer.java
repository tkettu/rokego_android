package com.teroki.rokego_helpers;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

/**
 * Created by Tero on 21.9.2017.
 */

public class PausableChronometer extends Chronometer {

    private long timeWhenStopped = 0;

    public PausableChronometer(Context context) {
        super(context);
    }

    public PausableChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PausableChronometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void start() {
        setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        timeWhenStopped = getBase() - SystemClock.elapsedRealtime();
    }

    public void reset(){
        stop();
        setBase(SystemClock.elapsedRealtime());
        timeWhenStopped = 0;
    }
}
