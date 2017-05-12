package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureContent;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureSizeUrl;
import com.srainbow.leisureten.util.BaseUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2017/4/19.
 */

public class BeautifulRVAdapter extends RecyclerView.Adapter<BeautifulRVAdapter.BeautifulHolder> implements View.OnClickListener{

    private Context mContext;
    private OnItemWithParamClickListener mIvInRvClickToDoListener;
    private List<PictureContent> imgListWithDescriptionList;
    public BeautifulRVAdapter(Context context, List<PictureContent> list) {
        this.mContext = context;
        this.imgListWithDescriptionList = list;
        for(PictureContent content : imgListWithDescriptionList){
            Log.e("adapter", content.getTitle() + content.getList().get(0).getMiddle());
        }
    }

    @Override
    public BeautifulHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_beautiful_base_cardview, parent, false);
        return new BeautifulHolder(view);
    }
    @Override
    public void onBindViewHolder(BeautifulHolder holder, int position) {
        List<PictureSizeUrl> sizeUrlList = imgListWithDescriptionList.get(position).getList();
////        Log.e("RvAdapter", sizeUrlList.get(BaseUtil.getInstance().getRandomIndex(sizeUrlList.size() - 1)).getSmall());
        Glide.with(mContext).load(sizeUrlList.get(BaseUtil.getInstance().getRandomIndex(sizeUrlList.size() - 1)).getMiddle())
                .into(holder.mIvShowPicture);
        holder.mTvDescription.setText(imgListWithDescriptionList.get(position).getTitle());
        holder.mRelative.setTag(sizeUrlList);
//        holder.mIvShowPicture.setTag(position);
//        List<String> urlList = new ArrayList<>();
//        urlList.add(imgListWithDescriptionList.get(position).getList().get(0).getMiddle());
//        holder.mIvShowPicture.setTag(1, urlList);
    }

    @Override
    public int getItemCount() {
        return imgListWithDescriptionList == null ? 0 : imgListWithDescriptionList.size();
    }

    @Override
    public void onClick(View v) {
        if(mIvInRvClickToDoListener != null){
            switch (v.getId()){
                case R.id.beautiful_base_rlayout:
                    mIvInRvClickToDoListener.onItemWithParamClick(v, v.getTag());
                    break;
            }
        }
    }

    public void setOnItemClickListener(OnItemWithParamClickListener listener){
        this.mIvInRvClickToDoListener = listener;
    }

    public static class BeautifulHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.beautiful_base_iv)
        ImageView mIvShowPicture;
        @Bind(R.id.beautiful_base_description_tv)
        TextView mTvDescription;
        @Bind(R.id.beautiful_base_rlayout)
        RelativeLayout mRelative;
        public BeautifulHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
