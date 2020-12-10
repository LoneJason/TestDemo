package com.example.testproject.base;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.testproject.MyBroadcast;
import com.example.testproject.R;

public class NotifyService extends Service
{

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNofication();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * 创建通知栏
     */
    private void createNofication(){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder ;
        int channelId = 1 ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){        //Android 8.0适配
            NotificationChannel channel = new NotificationChannel(String.valueOf(channelId),
                    "channel_name",
                    NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this,String.valueOf(channelId));
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        builder.setContentTitle("这是通知标题")            //指定通知栏的标题内容
                .setContentText("这是通知文本")             //通知的正文内容
                .setWhen(System.currentTimeMillis())                //通知创建的时间
                .setSmallIcon(R.drawable.ic_launcher_background)    //通知显示的小图标，只能用alpha图层的图片进行设置
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher_background));

        //设置操作意图对象
        Intent intent=new Intent(this, MyBroadcast.class);
        intent.setAction("mytest");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,intent,0);
        //设置布局
        RemoteViews mRemoteViews =new RemoteViews(getPackageName(), R.layout.notify_layout);    //初始化通知栏布局
        mRemoteViews.setOnClickPendingIntent(R.id.widget_album, pendingIntent);
        builder.setContent(mRemoteViews);     //通知栏设置布局
        Notification notification = builder.build() ;
        //channelId为本条通知的id
        startForeground(channelId,notification);
        manager.notify(channelId,notification);   //发送通知
    }
}
