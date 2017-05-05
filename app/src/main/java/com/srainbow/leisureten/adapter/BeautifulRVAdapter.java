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
import com.srainbow.leisureten.custom.interfaces.OnShowMoreIVInRvClickToDoListener;
import com.srainbow.leisureten.data.APIData.ImgListWithDescription;
import com.srainbow.leisureten.data.APIData.ShowApiPicContentDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2017/4/19.
 */

public class BeautifulRVAdapter extends RecyclerView.Adapter<BeautifulRVAdapter.BeautifulHolder> implements View.OnClickListener{

    private Context mContext;
    private OnShowMoreIVInRvClickToDoListener mIvInRvClickToDoListener;
    private List<ImgListWithDescription> imgListWithDescriptionList;
    public BeautifulRVAdapter(Context context, List<ImgListWithDescription> list) {
        this.mContext = context;
        this.imgListWithDescriptionList = list;
    }

    @Override
    public BeautifulHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_beautiful_base_cardview, parent, false);
        return new BeautifulHolder(view);
    }

    @Override
    public void onBindViewHolder(BeautifulHolder holder, int position) {
        Glide.with(mContext).load(imgListWithDescriptionList.get(position).getImgUrl().get(0).getMiddle()).into(holder.mIvShowPicture);
        holder.mTvDescription.setText(imgListWithDescriptionList.get(position).getImgDescription());
        List<String> urlList = new ArrayList<>();
        for(ShowApiPicContentDetail.PicSizeWithUrl picSizeWithUrl : imgListWithDescriptionList.get(position).getImgUrl()){
            urlList.add(picSizeWithUrl.getMiddle());
        }
        holder.mIvShowPicture.setTag(1, urlList);
    }

    @Override
    public int getItemCount() {
        return imgListWithDescriptionList == null ? 0 : imgListWithDescriptionList.size();
    }

    @Override
    public void onClick(View v) {
        if(mIvInRvClickToDoListener != null){
            switch (v.getId()){
                case R.id.beautiful_base_iv:
                    mIvInRvClickToDoListener.onIVItemClick(v, (List<String>)v.getTag(1));
                    break;
            }
        }
    }

    public void setOnItemClickListener(OnShowMoreIVInRvClickToDoListener listener){
        this.mIvInRvClickToDoListener = listener;
    }

    public static class BeautifulHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.beautiful_base_iv)
        ImageView mIvShowPicture;
        @Bind(R.id.beautiful_base_description_tv)
        TextView mTvDescription;
        public BeautifulHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
