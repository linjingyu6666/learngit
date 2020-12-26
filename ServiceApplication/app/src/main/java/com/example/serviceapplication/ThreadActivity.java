package com.example.serviceapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThreadActivity extends Activity implements View.OnClickListener {
    private TextView myText;

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
        switch(v.getId()){
            case R.id.change_text:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myText.setText("Nice to meet you");
                    }
                }).start();
                break;
                default:
                    break;
        }

    }

class LocalThread extends Thread{
    @Override
    public void run() {
    }
}
}