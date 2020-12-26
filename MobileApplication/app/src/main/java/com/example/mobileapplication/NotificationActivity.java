package com.example.mobileapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener{
    private String channelId="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Button button = findViewById(R.id.send_notice);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send_notice:
                Intent intent =new Intent(this,NoticeActivity.class);
                PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //高版本需要通道---“VERSION_CODES.O”处是字母O，而非数字0
             if(Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
                 //只在Android O之上需要通道，这里的第一个参数要和下面的channelId一样
                 NotificationChannel notificationChannel=new NotificationChannel(channelId,"name",NotificationManager.IMPORTANCE_HIGH);
                //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启通道，通知才能正常弹出
                     manager.createNotificationChannel(notificationChannel);
                }
                Notification notification =new NotificationCompat.Builder(this,channelId)
                    .setContentTitle("Title")
                    .setContentText("Content")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build();
                manager.notify(1,notification);
                break;
            default:
                break;
        }
    }
}
