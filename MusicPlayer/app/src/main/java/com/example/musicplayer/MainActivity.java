package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static TextView tv_progress;
    private static TextView tv_total;
    private ObjectAnimator animator;
    private Intent intent;
    private static SeekBar sb;
    private MyServiceConn conn;

    MusicService musicService;
    
    private boolean isUnbind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    private void init(){
        initSeekBar();
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_total = (TextView) findViewById(R.id.tv_total);

        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_continue_play).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);

        ImageView iv_music = (ImageView)findViewById(R.id.iv_music);
        animator = ObjectAnimator.ofFloat(iv_music,"rotation",0f,360.0f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);

        intent = new Intent(this,MusicService.class);/*创建意图对象*/
        conn =  new MyServiceConn();
        bindService(intent,conn,BIND_AUTO_CREATE);
    }




    private void initSeekBar() {
        sb = (SeekBar) findViewById(R.id.sb);

        /*为滑动条添加事件监听*/
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {/*滑动条进度改变时，会调用此方法*/
                if (progress == seekBar.getMax()){/*当滑动条到末端时，结束动画*/
                    animator.pause();/*停止播放动画*/
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {/*滑动条开始滑动时调用*/

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {/*滑动停止滑动时调用*/
                /*根据拖动的进度改变音乐播放进度*/
                int progress = seekBar.getProgress();
                musicService.seekTo(progress);/*改变播放速度*/

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:/*播放按钮点击事件*/
                musicService.play();
                animator.start();
                break;
            case R.id.btn_pause:/*暂停按钮点击事件*/
                musicService.pausePlay();
                animator.pause();
                break;
            case R.id.btn_continue_play:/*继续播放按钮点击事件*/
                musicService.continuePlay();
                animator.start();
                break;
            case R.id.btn_exit:/*退出按钮点击事件*/
                unbind(isUnbind);
                isUnbind = true;
                finish();
                break;
        }

    }

    private void unbind(boolean isUnbind) {
        if (!isUnbind){/*判断服务是否被解绑*/
            musicService.pausePlay();/*暂停播放音乐*/
            unbindService(conn);
        }
    }

    class MyServiceConn implements ServiceConnection { //用于实现连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicService = ((MusicService.MusicControl) service).getService();
            Log.d("", "musicService=" + musicService);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }


    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPostition = bundle.getInt("currentPosition");
            sb.setMax(duration);
            sb.setProgress(currentPostition);

            //歌曲的总时长
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMinute = null;
            String strSecond = null;

            if (minute < 10) {              //如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute; //在分钟的前面加一个0
            } else {
                strMinute = minute + "";
            }

            if (second < 10) {             //如果歌曲的时间中的秒钟小于10
                strSecond = "0" + second;//在秒钟前面加一个0
            } else {
                strSecond = second + "";
            }
            tv_total.setText(strMinute + ":" + strSecond);

            //歌曲当前播放时长
            minute = currentPostition / 1000 / 60;
            second = currentPostition / 1000 % 60;

            if (minute < 10) {             //如果歌曲的时间中的分钟小于10
                strMinute = "0" + minute;//在分钟的前面加一个0
            } else {
                strMinute = minute + "";
            }

            if (second < 10) {               //如果歌曲的时间中的秒钟小于10
                strSecond = "0" + second;  //在秒钟前面加一个0
            } else {
                strSecond = second + "";
            }

            tv_progress.setText(strMinute + ":" + strSecond);
        }


    };



}
