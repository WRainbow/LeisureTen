package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.custom.interfaces.OnTVInRvClickToDoListener;
import com.srainbow.leisureten.data.APIData.JokeDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2017/4/9.
 */

public class JokeRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private static final int TYPE_FOOTER = 1;//带有Footer
    private static final int TYPE_NORMAL = 2;//正常，不带Header与Footer

    private Context mContext;
    private List<JokeDetail> jokeDetails;
    private OnItemWithParamClickListener mItemInRvClickListener;

    public JokeRVAdapter(Context context, List<JokeDetail> details){
        mContext = context;
        this.jokeDetails = details;
    }

    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount()){
            return TYPE_FOOTER;
        }else{
            return TYPE_NORMAL;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_NORMAL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_joke_cradview, parent, false);
            return new JokeRVAdapter.ItemViewHolder(view);
        }else if(viewType == TYPE_FOOTER){
            View footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_pull_load_more, parent, false);
            return new JokeRVAdapter.FooterViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof JokeRVAdapter.ItemViewHolder){
            JokeRVAdapter.ItemViewHolder itemViewHolder = (JokeRVAdapter.ItemViewHolder)holder;
            itemViewHolder.mTvContentText.setText(jokeDetails.get(position).content);
            //set Collection ImageView tag
            itemViewHolder.mIvCollection.setTag(jokeDetails.get(position));
            //set Download ImageView background and tag
            itemViewHolder.mIvDownload.setImageResource(R.drawable.ic_copy);
            itemViewHolder.mIvDownload.setTag(jokeDetails.get(position).content);
            //setOnClickListener
            itemViewHolder.mIvCollection.setOnClickListener(this);
            itemViewHolder.mIvDownload.setOnClickListener(this);
        } else if(holder instanceof JokeRVAdapter.FooterViewHolder){
            JokeRVAdapter.FooterViewHolder footerViewHolder = (JokeRVAdapter.FooterViewHolder)holder;
            footerViewHolder.mTvLoadMore.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        return this.jokeDetails == null ? 0 : this.jokeDetails.size() + 1;
    }

    @Override
    public void onClick(View view) {
        if(mItemInRvClickListener != null){
            mItemInRvClickListener.onItemWithParamClick(view, view.getTag());
        }

    }

    public void setOnItemClickListener(OnItemWithParamClickListener listener){
        this.mItemInRvClickListener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.joke_rv_tv)
        TextView mTvContentText;
        //Collection and Download RelativeLayout
        @Bind(R.id.joke_include)
        RelativeLayout mRlayoutCollectionDownload;
        //Collection ImageView
        @Bind(R.id.layout_collection_iv)
        ImageView mIvCollection;
        @Bind(R.id.layout_download_iv)
        ImageView mIvDownload;
        public ItemViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.footer_loadmore_tv)
        TextView mTvLoadMore;
        public FooterViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
