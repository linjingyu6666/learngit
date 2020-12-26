package com.example.serviceapplication;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyService extends Service {

    private DownloadBinder mBinder = new DownloadBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind executed");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy executed");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind executed");
        return super.onUnbind(intent);
    }

    public void startDownload(){
        Log.d(TAG,"startDownload executed");
    }

    class DownloadBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }
}
