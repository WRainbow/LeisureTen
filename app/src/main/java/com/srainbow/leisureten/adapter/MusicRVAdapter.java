package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.data.realm.MusicInfo;
import com.srainbow.leisureten.util.BaseUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2016/10/12.
 */
public class MusicRVAdapter extends RecyclerView.Adapter<MusicRVAdapter.MusicViewHolder>
        implements View.OnClickListener {

    private Context mContex;
    private List<MusicInfo> musicDetailList;
    private OnItemWithParamClickListener onItemOnClickListener;

    public MusicRVAdapter(Context context, List<MusicInfo> list){
        this.mContex=context;
        this.musicDetailList=list;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_music_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        String duration = BaseUtil.getInstance().formatDuration(musicDetailList.get(position).getDuration());
        String size = BaseUtil.getInstance().formatSize(musicDetailList.get(position).getSize());
        holder.mTvMusicTitle.setText(musicDetailList.get(position).getTitle());
        holder.mTvMusicArtist.setText(musicDetailList.get(position).getArtist());
        holder.mTvMusicTime.setText(duration);
        holder.mTvMusicSize.setText(size);
        holder.mItemLayout.setOnClickListener(this);
        holder.mItemLayout.setTag(R.id.dataTag,musicDetailList.get(position));
        holder.mItemLayout.setTag(R.id.dataTag2, position);
    }

    @Override
    public int getItemCount() {
        return (musicDetailList==null?0:musicDetailList.size());
    }

    public void setOnItemClickListener(OnItemWithParamClickListener listener){
        this.onItemOnClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(onItemOnClickListener!=null){
            onItemOnClickListener.onItemWithParamClick(v, v.getTag(R.id.dataTag), v.getTag(R.id.dataTag2));
        }
    }

     static class MusicViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.music_title_tv) TextView mTvMusicTitle;
        @Bind(R.id.music_artist_tv) TextView mTvMusicArtist;
        @Bind(R.id.music_time_tv) TextView mTvMusicTime;
        @Bind(R.id.music_size_tv) TextView mTvMusicSize;
        @Bind(R.id.music_item_llayout) LinearLayout mItemLayout;
         MusicViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

     interface OnRecyclerViewItemClickListener{
        void onItemClick(View v, int position);
    }
}
