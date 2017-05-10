package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnTVWithUrlInRvClickToDoListener;
import com.srainbow.leisureten.data.APIData.TagDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2017/4/21.
 */

public class HDPictureTagRVAdapter extends RecyclerView.Adapter<HDPictureTagRVAdapter.HDPictureHolder> implements View.OnClickListener{

    private Context mContext;
    private List<TagDetail> tagDetailList;
    private OnTVWithUrlInRvClickToDoListener onTVWithUrlInRvClickToDoListener;

    public HDPictureTagRVAdapter(Context context, List<TagDetail> list){
        this.mContext = context;
        this.tagDetailList = list;
    }

    @Override
    public HDPictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_hd_picture_tag, parent, false);
        return new HDPictureHolder(view);
    }

    @Override
    public void onBindViewHolder(HDPictureHolder holder, int position) {
        holder.mTvHDTag.setText(tagDetailList.get(position).getTag());
        holder.mTvHDTag.setTag(tagDetailList.get(position).getUrl());
        holder.mTvHDTag.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return tagDetailList == null ? 0 : tagDetailList.size();
    }

    public void setOnItemClickListener(OnTVWithUrlInRvClickToDoListener listener){
        this.onTVWithUrlInRvClickToDoListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(onTVWithUrlInRvClickToDoListener != null){
            switch (v.getId()){
                case R.id.rv_hd_picture_tag_tv:
                    onTVWithUrlInRvClickToDoListener.onTvItemClick(v, (String)v.getTag());
                    break;
            }
        }
    }

    public static class HDPictureHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.rv_hd_picture_tag_tv)
        TextView mTvHDTag;
        public HDPictureHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
