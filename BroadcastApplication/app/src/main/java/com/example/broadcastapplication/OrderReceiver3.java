package com.example.broadcastapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OrderReceiver3 extends BroadcastReceiver {
    private final String TAG = "OrderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "OrderReceiver3 被调用了……");

        //取出Intent当中传递来的数据
        String msg = intent.getStringExtra("Msg");
        Log.d(TAG, "OrderReceiver3 接收到的数据： " + msg);
    }
}
