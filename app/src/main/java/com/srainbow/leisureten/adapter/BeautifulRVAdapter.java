package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamViewClickListener;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureContent;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureSizeUrl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow mItemWithParamClickListener 2017/4/19.
 */

public class BeautifulRVAdapter extends RecyclerView.Adapter<BeautifulRVAdapter.BeautifulHolder> implements View.OnClickListener{

    private Context mContext;
    private OnItemWithParamViewClickListener mItemWithParamViewClickListener;
    private List<PictureContent> pictureContentList;
    public BeautifulRVAdapter(Context context, List<PictureContent> list) {
        this.mContext = context;
        this.pictureContentList = list;
        for(PictureContent content : pictureContentList){
            Log.e("adapter", content.getTitle() + content.getList().get(0).getMiddle());
        }
    }

    @Override
    public BeautifulHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_beautiful_base_cardview, parent, false);
        return new BeautifulHolder(view);
    }
    @Override
    public void onBindViewHolder(final BeautifulHolder holder, int position) {
        List<PictureSizeUrl> sizeUrlList = pictureContentList.get(position).getList();
        //加载图片
        Glide.with(mContext).load(sizeUrlList.get((int) (Math.random() * sizeUrlList.size() - 1)).getMiddle())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.mIvShowPicture);
        //设置title
        holder.mTvDescription.setText(pictureContentList.get(position).getTitle());
        //设置进入图集布局的tag
        holder.mLlayoutInAtlas.setTag(R.id.dataTag, pictureContentList.get(position));
        //设置收藏按钮的tag
        holder.mIvCollection.setTag(R.id.dataTag, pictureContentList.get(position));
        holder.mIvCollection.setTag(R.id.viewTag, holder.mIvCollectionDown);
        //设置下载按钮的tag
        holder.mIvDownLoad.setTag(R.id.dataTag, pictureContentList.get(position));
        //设置取消收藏按钮的tag
        holder.mIvCollectionDown.setTag(R.id.dataTag, pictureContentList.get(position));
        holder.mIvCollectionDown.setTag(R.id.viewTag, holder.mIvCollection);
        //设置监听器
        holder.mLlayoutInAtlas.setOnClickListener(this);
        holder.mIvCollection.setOnClickListener(this);
        holder.mIvDownLoad.setOnClickListener(this);
        holder.mIvCollectionDown.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return pictureContentList == null ? 0 : pictureContentList.size();
    }

    @Override
    public void onClick(View v) {
        if(mItemWithParamViewClickListener != null){
            switch (v.getId()){
                case R.id.beautiful_base_in_llayout:
                    mItemWithParamViewClickListener.onItemWithParamViewClick(v, v.getTag(R.id.dataTag), null);
                    break;
                case R.id.layout_collection_iv:
                    mItemWithParamViewClickListener.onItemWithParamViewClick(v, v.getTag(R.id.dataTag),
                            (ImageView)v.getTag(R.id.viewTag));
                    break;
                case R.id.layout_download_iv:
                    mItemWithParamViewClickListener.onItemWithParamViewClick(v, v.getTag(R.id.dataTag), null);
                    break;
                case R.id.layout_collection_down_iv:
                    mItemWithParamViewClickListener.onItemWithParamViewClick(v, v.getTag(R.id.dataTag),
                            (ImageView)v.getTag(R.id.viewTag));
                    break;
            }
        }
    }

    public void setOnItemClickListener(OnItemWithParamViewClickListener listenerWithView){
        this.mItemWithParamViewClickListener = listenerWithView;
    }

    public static class BeautifulHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.beautiful_base_iv)
        ImageView mIvShowPicture;
        @Bind(R.id.beautiful_base_description_tv)
        TextView mTvDescription;
        @Bind(R.id.beautiful_base_rlayout)
        RelativeLayout mRelative;
        //进入图集按钮
        @Bind(R.id.beautiful_base_in_llayout)
        LinearLayout mLlayoutInAtlas;
        //收藏下载布局
        @Bind(R.id.beautiful_base_include)
        RelativeLayout mCollectionDownLoad;
        @Bind(R.id.layout_download_iv)
        ImageView mIvDownLoad;
        @Bind(R.id.layout_collection_iv)
        ImageView mIvCollection;
        @Bind(R.id.layout_collection_down_iv)
        ImageView mIvCollectionDown;

        public BeautifulHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
