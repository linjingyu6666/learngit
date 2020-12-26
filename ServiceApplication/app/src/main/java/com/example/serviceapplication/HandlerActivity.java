package com.example.serviceapplication;

import android.app.Activity;
import android.graphics.Color;
import android.net.wifi.aware.DiscoverySession;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class HandlerActivity extends Activity implements View.OnClickListener {
    private static final int UPDATE_TEXT = 1;
    private static final int UPDATE_BG= 2;
    private TextView myText;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case UPDATE_TEXT:
                    myText.setText("Nice to meet you");
                    break;
                case UPDATE_BG:
                    myText.setTextColor(Color.parseColor("#0000FF"));
                    break;
                default:
                    break;
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        myText = findViewById(R.id.mytext);
        Button change_text = findViewById(R.id.change_text);
        change_text.setOnClickListener(this);
        Button change_bd = findViewById(R.id.change_bg);
        change_bd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                }).start();
                break;

            case R.id.change_bg:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.what = UPDATE_BG;
                        handler.sendMessage(message);
                    }
                }).start();

            default:
                break;
        }

    }

}
