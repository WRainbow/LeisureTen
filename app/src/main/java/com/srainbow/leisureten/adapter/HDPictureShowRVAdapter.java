package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.data.APIData.ImgWithAuthor;
import com.srainbow.leisureten.widget.RectangleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2017/5/7.
 */

public class HDPictureShowRVAdapter extends RecyclerView.Adapter<HDPictureShowRVAdapter.HDPictureShowHolder>{

    private Context mContext;
    private List<ImgWithAuthor> imgWithAuthorList;

    public HDPictureShowRVAdapter(Context context, List<ImgWithAuthor> list){
        this.mContext = context;
        this.imgWithAuthorList = list;
    }

    @Override
    public HDPictureShowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_hd_picture_showpic, parent, false);
        return new HDPictureShowHolder(view);
    }

    @Override
    public void onBindViewHolder(HDPictureShowHolder holder, int position) {
        Log.e("adapterMsg", imgWithAuthorList.get(position).getImgUrl());
        Glide.with(mContext).load("http:" + imgWithAuthorList.get(position).getImgUrl()).override(100, 100).into(holder.mRectIv);
    }

    @Override
    public int getItemCount() {
        return imgWithAuthorList == null ? 0 : imgWithAuthorList.size();
    }

    public class HDPictureShowHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rv_hd_picture_showpic_riv)
        ImageView mRectIv;
        public HDPictureShowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
