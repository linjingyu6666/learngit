package com.example.broadcastapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class StaticReceiverActivity extends AppCompatActivity implements View.OnClickListener{
    private final String NORMAL_ACTION = "com.example.broadcastapplication.NormalReceiver";
    private final String ORDER_ACTION = "com.example.broadcastapplication.OrderReceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_receiver);

        Log.d("Mytest","test0");

        Button button_nbc = findViewById(R.id.send_NormalBC);
        button_nbc.setOnClickListener(this);

        Button button_obc = findViewById(R.id.send_OrderBC);
        button_obc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send_NormalBC:
                Intent intent_nbc = new Intent(NORMAL_ACTION);
                intent_nbc.putExtra("Msg", "This is a normal broadcast!");
                Log.d("Mytest","test1");
                sendBroadcast(intent_nbc);
                Log.d("Mytest","test2");
                break;
            case R.id.send_OrderBC:
                Intent intent_obc = new Intent(ORDER_ACTION);
                intent_obc.putExtra("Msg", "This is an order broadcast!");
                sendOrderedBroadcast(intent_obc, null);
        }
    }

}
