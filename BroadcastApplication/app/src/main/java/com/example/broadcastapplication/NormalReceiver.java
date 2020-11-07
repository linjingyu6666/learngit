package com.example.broadcastapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NormalReceiver extends BroadcastReceiver {
    private static final String TAG = "NormalReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Mytest","test3");
        String msg = intent.getStringExtra("Msg");
        Log.d(TAG, msg);
    }
}
