package com.example.cjcucsie.myapplication;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ServiceDemo extends Service {
    private int playPosition;//
    private ListView mListView;// 当前播放歌曲的序号
    private boolean IsPlay = false;//是否有歌曲在播放
    private Button playPause;
    private MyAdapter adapter;
    public MediaPlayer mediaPlayer;
    private List<Song> list;


    public class LocalBinder extends Binder
    {
        public ServiceDemo getService()
        {
            return ServiceDemo.this;

        }
    }
    public LocalBinder localBinder;

    public MediaPlayer mediaplayer;
    public String title;

    @Nullable
    public IBinder onBlind(Intent intent)
    {
        localBinder = new LocalBinder();
        return localBinder;
    }
    @Override
    public void onCreate()
    {
        System.out.println("fdf create service");
        mediaPlayer=new MediaPlayer();
        super.onCreate();
    }
    public void play(String path) {
        //播放之前要先把音频文件重置
        try {
            mediaPlayer.reset();
            //调用方法传进去要播放的音频路径
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            //异步准备音频资源

            //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();//开始音频
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void control(View view) {
            switch (view.getId()) {
            case R.id.playing_btn_previous://上一曲
                //如果播放歌曲的序号小于或者等于0的话点击上一曲就提示已经是第一首了
                if (playPosition <= 0) {
                    Toast.makeText(ServiceDemo.this, "已经是第一首歌了", Toast.LENGTH_SHORT).show();
                } else {
                    //让歌曲的序号减一
                    playPosition--;
                    //播放
                    play(list.get(playPosition).path);
                    playPause.setBackgroundResource(R.drawable.pause_press);
                }
                break;
            case R.id.playing_btn_pause://暂停和播放
                if (IsPlay == false) {
                    //播放暂停按钮图片变成播放状态
                    playPause.setBackgroundResource(R.drawable.pause_press);
                    //继续播放
                    mediaPlayer.start();
                    IsPlay = true;//是否在播放赋值为true

                } else {
                    //播放暂停按钮图片变成暂停状态
                    playPause.setBackgroundResource(R.drawable.play_press);
                    //暂停歌曲
                    mediaPlayer.pause();
                    IsPlay = false;//是否在播放赋值为false

                }
                break;
            case R.id.playing_btn_next://下一曲
                //歌曲序号大于或者等于歌曲列表的大小-1时,让歌曲序号为第一首
                if (playPosition >= list.size() - 1) {
                    playPosition = 0;
                } else {
                    //点击下一曲让歌曲的序号加一
                    playPosition++;
                }
                //播放
                play(list.get(playPosition).path);
                //播放暂停按钮图片变成播放状态
                playPause.setBackgroundResource(R.drawable.pause_press);
                break;
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
        }
//        Bundle bundle = intent.getExtras();
//        String path = bundle.getString("path");
        System.out.println("fdf service");
        try {
            System.out.println("fdf service0");
            if(mediaPlayer != null) {
                mediaPlayer.reset();
            }
            System.out.println("fdf service1 ");
//            mediaPlayer.setDataSource(this, Uri.parse(path));
//            mediaPlayer.setDataSource(path);
            System.out.println("fdf service2");
           mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
               @Override
               public void onPrepared(MediaPlayer mediaPlayer) {
                   mediaPlayer.start();
               }
           });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }
}
