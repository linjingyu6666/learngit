package com.example.mediaapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        Button play_btn = findViewById(R.id.play_btn);
        play_btn.setOnClickListener(this);
        Button pause_btn = findViewById(R.id.pause_btn);
        pause_btn.setOnClickListener(this);
        Button stop_btn = findViewById(R.id.stop_btn);
        stop_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_btn:
                try {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer = MediaPlayer.create(this,R.raw.last_stop);
                    }
                    mediaPlayer.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.pause_btn:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.stop_btn:
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
