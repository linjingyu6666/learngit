package com.example.broadcastapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OrderReceiver1 extends BroadcastReceiver {
    private final String TAG = "OrderReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "OrderReceiver1 被调用了……");

        //取出Intent当中传递来的数据
        String msg = intent.getStringExtra("Msg");
        Log.d(TAG, "OrderReceiver1 接收到的数据： " + msg);

        //向下一优先级的Receiver传递数据
        Bundle bundle = new Bundle();
        bundle.putString("Data", "OrderReceiver1 转发下一站数据");
        setResultExtras(bundle);
    }
}
