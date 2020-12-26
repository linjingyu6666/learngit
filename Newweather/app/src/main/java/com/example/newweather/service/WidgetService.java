package com.example.newweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.he.coolweather2.layout.Widget;

/**
 * Created by he on 2017/1/22.
 */

public class WidgetService extends Service {

    private static final String TAG = "WidgetService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        updateWidget();
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int anHour = 1 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, WidgetService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return START_STICKY_COMPATIBILITY;
    }


    //更新桌面小部件的信息
    private void updateWidget() {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        ComponentName cn = new ComponentName(getApplicationContext(), Widget.class);
        Widget.updateAppWidget(this, manager, cn, null);
    }


    @Override
    public void onDestroy() {
        Intent i = new Intent(this, WidgetService.class);
        this.startService(i);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
