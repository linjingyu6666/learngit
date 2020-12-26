package com.example.newmusicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1=(Button)findViewById(R.id.danji);
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });


        playBtn=(Button)findViewById(R.id.playBtn);
        //歌曲开始/剩余时间
        elapsedTimeLabel=(TextView)findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel=(TextView)findViewById(R.id.remainingTimeLabel);

        //media player
        mp=MediaPlayer.create(this,R.raw.music1);
        totalTime=mp.getDuration();
        mp=MediaPlayer.create(this,R.raw.music);
        totalTime=mp.getDuration();

        //position bar
        positionBar=(SeekBar)findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp !=null){
                    try {
                        Message msg=new Message();
                        msg.what=mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                }
            }
        }).start();

    }


    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Update positionBar
            positionBar.setProgress(currentPosition);

            //Update Labels
//            String elapsedTime=createTimeLabel(currentPosition);
//            elapsedTimeLabel.setText(elapsedTime);
//
//            String remainTime=createTimeLabel(totalTime-currentPosition);
//            remainingTimeLabel.setText("- " + remainTime);
        }
    };


    public void playBtnClick(View view) {
        if(!mp.isPlaying()) {
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);

        }else{
            //playing
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }


}
