package com.example.broadcastapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OrderReceiver2 extends BroadcastReceiver {
    private final String TAG = "OrderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "OrderReceiver2 被调用了……");

        //取出上一优先级的Receiver传递来的数据
        String data = getResultExtras(true).getString("Data");
        Log.d(TAG, "OrderReceiver2 接收到的数据： " + data);

        //取出Intent当中传递来的数据
        String msg = intent.getStringExtra("Msg");
        Log.d(TAG, "OrderReceiver2 接收到的数据： " + msg);

        //截断有序广播
        abortBroadcast();
    }
}
