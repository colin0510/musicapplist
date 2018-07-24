package com.example.cjcucsie.myapplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Song> list;
    private MainActivity mainActivity;
    ServiceDemo ss;
    public MyAdapter(MainActivity mainActivity, List<Song> list) {
        this.list = list;
        System.out.print("fdf listsize" + list.size());
        this.mainActivity = mainActivity;

    }
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_music_listview,parent,false);
        System.out.println("fdf ");
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.ViewHolder holder, @SuppressLint("RecyclerView")final int position)
    {
        holder.song.setText(list.get(position).song);

       holder.song.setOnClickListener(new View.OnClickListener(){

           @Override
           public void onClick(View view) {
               Intent intent = new Intent(mainActivity,ServiceDemo.class);
               Bundle bundle = new Bundle();
               bundle.putString("path",list.get(position).path);
               System.out.println("path:" + list.get(position).path);
               intent.putExtras(bundle);
               mainActivity.startService(intent);
               ss.play(list.get(position).path);

           }
       });
    }

//    @Override
//    public long getItemId(int i) {
//        return i;
//    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView song;//歌曲名
        TextView singer;//歌手
        TextView duration;//时长
        TextView position;//序号

        public ViewHolder(View v) {
            super(v);
            song = v.findViewById(R.id.item_mymusic_song);
        }
    }

}