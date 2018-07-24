package com.example.cjcucsie.myapplication;

import android.Manifest;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    public List<Song> list;
    private MyAdapter adapter;
    private int playPosition;//当前播放歌曲的序号
    private boolean IsPlay = false;//是否有歌曲在播放
    private Button playPause;//暂停和播放按钮
    private MyAdapter mAdaper;
    private RecyclerView mRecyclerView;
    private TextView song;//歌曲名
    private TextView singer;//歌手名
    TextView title_name;
    int totaltime;
    TextView end_time;
    ServiceConnection mSc;
    ServiceDemo serviceDemo;
    SeekBar time_bar;
    TextView locate_time;
//    private ImageView imageView;//控制台的图片
//    private Animation animation;//动画
 ServiceDemo ss;
    public void control(View view) {

        switch (view.getId()) {
            case R.id.playing_btn_previous://上一曲
                //如果播放歌曲的序号小于或者等于0的话点击上一曲就提示已经是第一首了
                if (playPosition <= 0) {
                    Toast.makeText(MainActivity.this, "已经是第一首歌了", Toast.LENGTH_SHORT).show();
                } else {
                    //让歌曲的序号减一
                    playPosition--;
                    //播放
                    ss.play(list.get(playPosition).path);
                    playPause.setBackgroundResource(R.drawable.pause_press);
                }
                break;
            case R.id.playing_btn_pause://暂停和播放
                if (IsPlay == false) {
                    //播放暂停按钮图片变成播放状态
                    playPause.setBackgroundResource(R.drawable.pause_press);
                    //继续播放
                    serviceDemo.mediaPlayer.start();
                    IsPlay = true;//是否在播放赋值为true

                } else {
                    //播放暂停按钮图片变成暂停状态
                    playPause.setBackgroundResource(R.drawable.play_press);
                    //暂停歌曲
                    serviceDemo.mediaPlayer.pause();
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
                serviceDemo.play(list.get(playPosition).path);
                //播放暂停按钮图片变成播放状态
                playPause.setBackgroundResource(R.drawable.pause_press);
                break;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playPause = findViewById(R.id.playing_btn_pause);
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    0 );
        }

        mSc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
               ss = ((ServiceDemo.LocalBinder) iBinder).getService();
                serviceDemo = ss;
                mAdaper.ss=ss;
                if(!ss.mediaplayer.isPlaying())
                {
                    playPause.setBackgroundResource(R.drawable.pause_press);
                }
                else
                {
                    playPause.setBackgroundResource(R.drawable.play_press);
                }
                totaltime = ss.mediaplayer.getDuration();
                time_bar.setMax(totaltime);
                time_bar.setProgress(ss.mediaplayer.getCurrentPosition());
                title_name.setText(ss.title);
                locate_time.setText(createTimeString(ss.mediaplayer.getCurrentPosition()));
                end_time.setText(createTimeString(totaltime - ss.mediaplayer.getCurrentPosition()));
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                unbindService(mSc);
            }
        };
        initView();
        startService();
        bindService();
    }

    /**
     * 初始化view
     */
    private void initView() {

        list = new ArrayList<>();
        //把扫描到的音乐赋值给list
        list = MusicUtils.getMusicData(this);
        mAdaper = new MyAdapter(this,list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdaper);
    }
    /**
     * 播放音频的方法
     */


    public String createTimeString(int time)
    {
        String s = "";
        int min = time/1000/60;
        int sec = time/100%60;
        s += min + ":";
        s = sec < 10 ? s + "0" + sec : s + sec;
        return s;
    }
    private void setText() {
        song.setText(list.get(playPosition).song);
        song.setSelected(true);//当歌曲名字太长是让其滚动
        singer.setText(list.get(playPosition).singer);

    }
}