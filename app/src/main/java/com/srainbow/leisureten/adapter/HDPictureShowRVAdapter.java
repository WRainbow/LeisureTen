package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnItemClickListener;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamViewClickListener;
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
    private OnItemWithParamViewClickListener mItemInRvClickToDoListener;
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
        //set Collection ImageView tag
        holder.mIvCollection.setTag(R.id.dataTag, imgWithAuthorList.get(position));
        holder.mIvCollection.setTag(R.id.viewTag, holder.mIvCollectionDown);
        //set Collection Down ImageView tag
        holder.mIvCollectionDown.setTag(R.id.dataTag, imgWithAuthorList.get(position));
        holder.mIvCollectionDown.setTag(R.id.viewTag, holder.mIvCollection);
        //set Download ImageView tag
        holder.mIvDownload.setTag(R.id.dataTag, imgWithAuthorList.get(position));
        // setOnClickListener
        holder.mIvCollection.setOnClickListener(this);
        holder.mIvCollectionDown.setOnClickListener(this);
        holder.mIvDownload.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return imgWithAuthorList == null ? 0 : imgWithAuthorList.size();
    }

    public void setOnItemClickListener(OnItemWithParamViewClickListener listener){
        this.mItemInRvClickToDoListener = listener;
    }

    @Override
    public void onClick(View v) {
        if(mItemInRvClickToDoListener != null){
            switch (v.getId()){
                //collection imageView clicked
                case R.id.layout_collection_iv:
                    mItemInRvClickToDoListener.onItemWithParamViewClick(v, v.getTag(R.id.dataTag),
                            (ImageView)v.getTag(R.id.viewTag));
                    break;
                //cancel collection imageView clicked
                case R.id.layout_collection_down_iv:
                    mItemInRvClickToDoListener.onItemWithParamViewClick(v, v.getTag(R.id.dataTag),
                            (ImageView)v.getTag(R.id.viewTag));
                    break;
                //download imageView clicked
                case R.id.layout_download_iv:
                    mItemInRvClickToDoListener.onItemWithParamViewClick(v, v.getTag(R.id.dataTag), null);
                    break;
            }
        }
    }

    public class HDPictureShowHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rv_hd_picture_showpic_iv)
        ImageView mRectIv;
        @Bind(R.id.rv_hd_picture_author_tv)
        TextView mTvAuther;

        //Collection and DownLoad RelativeLayout
        @Bind(R.id.rv_hd_picture_include)
        RelativeLayout mRlayoutCollectionDownLoad;
        //Collection ImageView
        @Bind(R.id.layout_collection_iv)
        ImageView mIvCollection;
        //Download ImageView
        @Bind(R.id.layout_collection_down_iv)
        ImageView mIvCollectionDown;
        @Bind(R.id.layout_download_iv)
        ImageView mIvDownload;
        public HDPictureShowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
