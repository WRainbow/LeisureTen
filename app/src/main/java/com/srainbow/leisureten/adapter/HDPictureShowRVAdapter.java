package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnItemClickListener;
import com.srainbow.leisureten.data.APIData.ImgWithAuthor;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2017/5/7.
 */

public class HDPictureShowRVAdapter extends RecyclerView.Adapter<HDPictureShowRVAdapter.HDPictureShowHolder>
        implements View.OnClickListener{

    private Context mContext;
    private OnItemClickListener itemClickListener;
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
        Glide.with(mContext).load("http:" + imgWithAuthorList.get(position).getImgUrl()).into(holder.mRectIv);
        holder.mTvAuther.setText(String.format(
                mContext.getResources().getString(R.string.imgAuthor), imgWithAuthorList.get(position).getImgAuthor()));
    }

    @Override
    public int getItemCount() {
        return imgWithAuthorList == null ? 0 : imgWithAuthorList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener != null){
            itemClickListener.onItemClick(v);
//            switch (v.getId()){
//                case R.id.rv_hd_picture_showpic_iv:
//
//                    break;
//                case R.id.rv_hd_picture_collection_iv:
//                    break;
//                case R.id.rv_hd_picture_download_iv:
//                    break;
//            }
        }
    }

    public class HDPictureShowHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rv_hd_picture_showpic_iv)
        ImageView mRectIv;
        @Bind(R.id.rv_hd_picture_author_tv)
        TextView mTvAuther;
        public HDPictureShowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
