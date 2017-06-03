package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.data.realm.SongMenu;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2017/6/1.
 */

public class SongMenuRVAdapter extends RecyclerView.Adapter<SongMenuRVAdapter.SongMenuHolder>
        implements View.OnClickListener, View.OnLongClickListener{

    private List<SongMenu> menuList;
    private Context mContext;
    private OnItemWithParamClickListener onItemOnClickListener;

    public SongMenuRVAdapter(Context context, List<SongMenu> list) {
        this.mContext = context;
        this.menuList = list;
    }

    @Override
    public SongMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_song_menu_name, parent, false);
        return new SongMenuHolder(view);
    }

    @Override
    public void onBindViewHolder(SongMenuHolder holder, int position) {
        holder.mTvSongName.setText(menuList.get(position).getMenuName());
        holder.mTvSongName.setTag(R.id.dataTag, menuList.get(position).getMenuId());
        holder.mTvSongName.setTag(R.id.dataTag2, position);
        holder.mTvSongName.setTag(R.id.dataTagString, menuList.get(position).getMenuName());
        holder.mTvSongName.setOnClickListener(this);
        //默认歌单不设置长按事件
        if (position > 0) {
            holder.mTvSongName.setOnLongClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return menuList == null ? 0 : menuList.size();
    }

    public void setItemOnClickListener(OnItemWithParamClickListener listener) {
        this.onItemOnClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (onItemOnClickListener != null) {
           switch (v.getId()) {
               case R.id.song_menu_name_tv:
                   onItemOnClickListener.onItemWithParamClick(v, v.getTag(R.id.dataTag),
                           v.getTag(R.id.dataTag2), v.getTag(R.id.dataTagString));
                   break;
           }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onItemOnClickListener != null) {
            switch (v.getId()) {
                case R.id.song_menu_name_tv:
                    onItemOnClickListener.onItemWithParamLongClick(v, v.getTag(R.id.dataTag),
                            v.getTag(R.id.dataTag2), v.getTag(R.id.dataTagString));
                    break;
            }
        }
        return false;
    }

    static class SongMenuHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.song_menu_name_tv)
        TextView mTvSongName;

        SongMenuHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
