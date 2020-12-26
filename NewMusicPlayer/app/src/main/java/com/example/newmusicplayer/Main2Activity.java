package com.example.newmusicplayer;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import android.os.Bundle;

import android.text.Editable;

import android.text.TextWatcher;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.R.id.list;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences sharedPreferences;
    ImageView nextIv,playIv,lastIv,imageview_playstyle,image_loading,love;
    Animation animation;
    TextView singerTv,songTv;
    RecyclerView musicRv;
    private SeekBar seekBar;
    private boolean ischanging = false;
    private ObjectAnimator mCircleAnimator;
    int totalTime;



    private SearchView mSearchView;
    // 用于判断当前的播放顺序，0->单曲循环,1->顺序播放,2->随机播放
    private int play_style = 0;


    //数据源
    List<LocalMusicBean>mDatas,filterString;
    private LocalMusicAdapter adapter;
    //private int position;
    //记录当前正在播放的音乐的位置
    int currentPlayPosition=-1;

    //记录暂停音乐时播放的进度条
    int currentPausePositionInSong=0;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        //隐藏顶部的标题栏
        ActionBar actionBar=getSupportActionBar();
        if(actionBar !=null){
            actionBar.hide();
        }

        ImageView Image=(ImageView)findViewById(R.id.local_music_bottom_iv_icon);
        Image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, PlayActivity.class);
                startActivity(intent);
            }
        });


        initView();

        mediaPlayer=new MediaPlayer();
        mDatas=new ArrayList<>();
        //创建适配器对象
        adapter = new LocalMusicAdapter(this, mDatas);
        musicRv.setAdapter(adapter);

        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        musicRv.setLayoutManager(layoutManager);
        //
        registerForContextMenu(musicRv);
        //******************************************************************************************
        image_loading =(ImageView) findViewById(R.id.local_music_bottom_iv_icon);
        //
        mCircleAnimator = ObjectAnimator.ofFloat(image_loading, "rotation", 0.0f, 360.0f);
        mCircleAnimator.setDuration(60000);
        mCircleAnimator.setInterpolator(new LinearInterpolator());
        mCircleAnimator.setRepeatCount(-1);
        mCircleAnimator.setRepeatMode(ObjectAnimator.RESTART);
        //*************************************************************************************
        //设置播放方式
        imageview_playstyle = (ImageView) this.findViewById(R.id.play_style);
        imageview_playstyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_style++;
                if (play_style > 2) {
                    play_style = 0;
                }

                switch (play_style) {
                    case 0:
                        imageview_playstyle.setImageResource(R.mipmap.icon_dan);
                        Toast.makeText(Main2Activity.this, "单曲循环",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        imageview_playstyle.setImageResource(R.mipmap.icon_xun);
                        Toast.makeText(Main2Activity.this, "顺序播放",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        imageview_playstyle.setImageResource(R.mipmap.icon_sui);
                        Toast.makeText(Main2Activity.this, "随机播放",
                                Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        });




        //加载本地数据源
        loadLocalMusicData();
        //设置每一项的点击事件
        setEventListener();


        //播放完毕后自动播放下一曲的方式************************************************************
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                switch (play_style){
                    case 0:
                        //播放当前播放的音乐
                        LocalMusicBean Bean1 = mDatas.get(currentPlayPosition);
                        playMusicInMusicBean(Bean1);
                        break;
                    case 1:
                        //自动播放下一首音乐
                        LocalMusicBean Bean2 = mDatas.get(currentPlayPosition+1);
                        playMusicInMusicBean(Bean2);
                        break;
                    case 2:
                        //随机播放下一首音乐
                        Random ran0=new Random();
                        int i=ran0.nextInt(5);
                        LocalMusicBean Bean3 = mDatas.get(i);
                        playMusicInMusicBean(Bean3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Update positionBar
            seekBar.setProgress(currentPosition);
        }
    };


    private void setEventListener(){
        /*设置每一项的点击事件*/
        adapter.setOnItemClickListener(new LocalMusicAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                currentPlayPosition=position;
                //**********************************************************************************
                LocalMusicBean musicBean = mDatas.get(position);
                playMusicInMusicBean(musicBean);

            }
        });
        adapter.setOnItemLongClickListener(new LocalMusicAdapter.OnItemLongClickListener(){
            @Override
            public void onItemLongClick(View view, int position) {
                showPopMenu(view,position);
            }
        });

    }

    public void playMusicInMusicBean(LocalMusicBean musicBean) {
        /*根据传入对象播放音乐*/
        //设置底部显示的歌手名称和歌曲名
        singerTv.setText(musicBean.getSinger());
        songTv.setText(musicBean.getSong());

        seekBar=(SeekBar)findViewById(R.id.positionBar2);

        stopMusic();
        //重置多媒体播放器
        mediaPlayer.reset();
        //////////////////
        seekBar.setProgress(0);

        //设置新的路径
        try {
            mediaPlayer.setDataSource(musicBean.getPath());
            playMusic();

        } catch (IOException e) {
            e.printStackTrace();
        }



        totalTime=mediaPlayer.getDuration();
        seekBar.setMax(totalTime);

        //歌曲进程条变换
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mediaPlayer.seekTo(progress);
                            seekBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer !=null){
                    try {
                        Message msg=new Message();
                        msg.what=mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                }
            }
        }).start();

    }

    /*
    * 点击播放按钮播放音乐，或者暂停音乐重新播放
    * 播放音乐有两种情况
    * 1：从暂停到播放
    * 2：从停止到播放*/
    @TargetApi(Build.VERSION_CODES.KITKAT) private void playMusic() {
        /*播放音乐的函数*/
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            if (currentPausePositionInSong==0) {

                try {
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    //播放歌曲时图片旋转
//                    animation=AnimationUtils.loadAnimation(this,R.anim.loading_anim);
//                    image_loading.startAnimation(animation);
                    mCircleAnimator.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                //从暂停到播放
                mediaPlayer.seekTo(currentPausePositionInSong);
                mediaPlayer.start();
                mCircleAnimator.resume();
            }

            playIv.setImageResource(R.mipmap.icon_pause);
        }

    }

    private void pauseMusic() {
        /*暂停音乐函数*/
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            currentPausePositionInSong=mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
            mCircleAnimator.pause();
            playIv.setImageResource(R.mipmap.icon_play);
        }
    }

    private void stopMusic() {
        /*停止音乐的函数*/
        if(mediaPlayer!=null){
            currentPausePositionInSong=0;
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            mediaPlayer.stop();
            playIv.setImageResource(R.mipmap.icon_play);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    private void loadLocalMusicData() {
        /*加载本地存储但当中的音乐文件到集合当中*/
        //获取ContentResolver对象
        ContentResolver resolver = getContentResolver();
        //获取本地音乐存取的uri地址
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //开始查询地址
        Cursor cursor = resolver.query(uri, null, null, null, null);
        //便利Cursor
        int id=0;
        while(cursor.moveToNext()){
            String song = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            id++;
            String sid = String.valueOf(id);
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(duration));
            //将一行当中的数据封装对象当中
            LocalMusicBean bean = new LocalMusicBean(sid, song, singer, album, time, path);
            //封装到集合当中
            mDatas.add(bean);
        }
        //数据源变化，提示适配器更新
        adapter.notifyDataSetChanged();
    }

    private void initView(){
        //初始化控件的函数
        nextIv =(ImageView)findViewById(R.id.local_music_bottom_iv_next);
        playIv=(ImageView)findViewById(R.id.local_music_bottom_iv_play);
        lastIv=(ImageView)findViewById(R.id.local_music_bottom_iv_last);
        singerTv=(TextView)findViewById(R.id.local_music_bottom_tv_singer);
        songTv=(TextView)findViewById(R.id.local_music_bottom_tv_song);
        love =(ImageView)findViewById(R.id.icon_insertlove);
        musicRv=(RecyclerView)findViewById(R.id.local_music_rv);
        nextIv.setOnClickListener(this);
        lastIv.setOnClickListener(this);
        playIv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Random ran1=new Random();
        switch (view.getId()){
            case R.id.local_music_bottom_iv_last:
                if(play_style==0){
                    LocalMusicBean lastBean = mDatas.get(currentPlayPosition);
                    playMusicInMusicBean(lastBean);
                    break;
                }else if(play_style==1){
                    if (currentPlayPosition==0) {
                        Toast.makeText(this,"已经是第一首了，即将从最后一首播放",Toast.LENGTH_SHORT).show();
                        //*****************************************************************************
                        currentPlayPosition=mDatas.size()-1;
                        LocalMusicBean lastBean = mDatas.get(currentPlayPosition);
                        playMusicInMusicBean(lastBean);
                        return;
                    }
                    currentPlayPosition = currentPlayPosition-1;
                    LocalMusicBean lastBean = mDatas.get(currentPlayPosition);
                    playMusicInMusicBean(lastBean);
                    break;
                }else {
                    currentPlayPosition=ran1.nextInt(5);
                    LocalMusicBean lastBean = mDatas.get(currentPlayPosition);
                    playMusicInMusicBean(lastBean);
                    break;
                }

            case R.id.local_music_bottom_iv_next:
                if(play_style==0)
                {
                    LocalMusicBean nextBean = mDatas.get(currentPlayPosition);
                    playMusicInMusicBean(nextBean);
                    break;
                }else if (play_style==1){
                    if (currentPlayPosition==mDatas.size()-1) {
                        Toast.makeText(this,"已经是最后一首了，即将从第一首播放",Toast.LENGTH_SHORT).show();
                        //**************************************************************************
                        currentPlayPosition=0;
                        LocalMusicBean nextBean = mDatas.get(currentPlayPosition);
                        playMusicInMusicBean(nextBean);
                        return;
                    }
                    currentPlayPosition = currentPlayPosition+1;
                    LocalMusicBean nextBean = mDatas.get(currentPlayPosition);
                    playMusicInMusicBean(nextBean);
                    break;
                }else {
                    currentPlayPosition=currentPlayPosition+ran1.nextInt(mDatas.size()-1);
                    currentPlayPosition %= mDatas.size();
                    LocalMusicBean nextBean = mDatas.get(currentPlayPosition);
                    playMusicInMusicBean(nextBean);
                    break;
                }

            case R.id.local_music_bottom_iv_play:
                if (currentPlayPosition==-1) {
                    //并未选中要播放的音乐
                    Toast.makeText(this, "请选择想要播放的音乐", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mediaPlayer.isPlaying()) {
                    //此时处于播放状态，需要暂停音乐
                    pauseMusic();
                }else{
                    //此时未播放音乐，点击开始播放音乐
                    playMusic();
                }
                break;
        }
    }





    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

//    //*********************************************************************************************
public void showPopMenu(View view,final int pos){
    PopupMenu popupMenu = new PopupMenu(this,view);
    popupMenu.getMenuInflater().inflate(R.menu.menu_item,popupMenu.getMenu());
    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.removeItem:
                    love=(ImageView) findViewById(R.id.icon_insertlove);
                    love.setImageResource(R.mipmap.love_1);
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.removeItem2:
                    love=(ImageView) findViewById(R.id.icon_insertlove);
                    love.setImageResource(R.mipmap.love_0);
                    Toast.makeText(getApplicationContext(), "取消成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.removeItem3:
                    LocalMusicBean Bean2 = mDatas.get(currentPlayPosition);
                    playMusicInMusicBean(Bean2);
                    break;
            }
            return false;
        }
        //        public boolean onMenuItemClick(MenuItem item) {
//            love=(ImageView) findViewById(R.id.icon_insertlove);
//            love.setImageResource(R.mipmap.love_1);
//            return true;
//        }
    });
    popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
        @Override
        public void onDismiss(PopupMenu menu) {
            //Toast.makeText(getApplicationContext(), "更改成功", Toast.LENGTH_SHORT).show();
        }
    });
    popupMenu.show();
}


}
