package com.example.testproject.media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceView;

import com.example.testproject.MainActivity;

import java.io.IOException;

/**
 * 本地多媒体库的访问，使用到ContentResolver内容接收者，进行跨进程通信，这个APP就是一个进程，获多媒体库在别的进程中
 *  这个demo可以学习多媒体库的访问 也可以学ContentResolver跨进程通信
 */
public class MyMedia
{
    MediaPlayer player;
    //查询本地音乐
    public void GetMusic(Context context) {
        ContentResolver resolver = context.getContentResolver();
        String[] projection = {"_data", "_display_name", "_size", "mime_type", "title", "duration"};
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  //查询音频地址
        Cursor audioCursor = resolver.query(audioUri, projection, null, null, null);
        audioCursor.moveToFirst();
        while (audioCursor.moveToNext()) {
            //获取歌曲信息
            Log.d("testProject", audioCursor.getString(audioCursor.getColumnIndex("_data")));   //歌曲文件的全路径
            Log.d("testProject", audioCursor.getString(audioCursor.getColumnIndex("_display_name")));  //歌曲文件的名称
            Log.d("testProject", audioCursor.getString(audioCursor.getColumnIndex("_size")));   //歌曲文件的大小
            Log.d("testProject", audioCursor.getString(audioCursor.getColumnIndex("mime_type")));   //格式
            Log.d("testProject", audioCursor.getString(audioCursor.getColumnIndex("title")));  //歌曲的名称

        }
        audioCursor.close();
    }

    //查询本地视频
    public void GetVideo(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;   //查询视频地址
        //创建查询视频游标
        Cursor cursor = resolver.query(uri, null, null,
                null,
                null
        );
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            //获取视频封面
//                    long id = cursor.getLong(0); // 取得视频的ID，因为我上行只有一个参数所以下标为0
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(resolver, id, MediaStore.Video.Thumbnails.MINI_KIND, options);
//                    list.add(bitmap);

            Log.d("testProject", cursor.getString(cursor.getColumnIndex("_data")));
            Log.d("testProject", cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)));


        }
        cursor.close();
    }

    //多媒体播放器
    public void ShowMediaPlayer(SurfaceView surfaceView)
    {

        if (player == null) {
            player = new MediaPlayer();
        }
        player.reset();
        try {
            player.setDataSource("/storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1561527468226.mp4");
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置播放声音类型
            player.setDisplay(surfaceView.getHolder());//设置在surfaceView上播放
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();//准备需要一段时间，所以用监听
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
