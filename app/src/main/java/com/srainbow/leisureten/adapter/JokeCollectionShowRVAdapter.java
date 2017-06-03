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
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamViewClickListener;
import com.srainbow.leisureten.data.apidata.juhe.joke.JokeDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by SRainbow on 2017/4/9.
 */

public class JokeCollectionShowRVAdapter extends RecyclerView.Adapter<JokeCollectionShowRVAdapter.JokeHolder> implements View.OnClickListener{

    private Context mContext;
    private List<JokeDetail> jokeDetails;
    private OnItemWithParamViewClickListener mItemInRvClickListener;

    public JokeCollectionShowRVAdapter(Context context, List<JokeDetail> details){
        mContext = context;
        this.jokeDetails = details;
    }

    @Override
    public JokeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_joke_cradview, parent, false);
        return new JokeHolder(view);
    }

    @Override
    public void onBindViewHolder(JokeHolder holder, int position) {
        holder.mTvContentText.setText(jokeDetails.get(position).content);
        //set Collection Down ImageView tag
        holder.mIvCollectionDown.setTag(R.id.dataTag, jokeDetails.get(position));
        holder.mIvCollectionDown.setTag(R.id.dataTag2, position);
        //set Download ImageView background and tag
        holder.mIvCopy.setImageResource(R.drawable.ic_copy);
        holder.mIvCopy.setTag(R.id.dataTag, jokeDetails.get(position).content);
        //setOnClickListener
        holder.mIvCollectionDown.setOnClickListener(this);
        holder.mIvCopy.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return this.jokeDetails == null ? 0 : this.jokeDetails.size();
    }

    @Override
    public void onClick(View view) {
        if(mItemInRvClickListener != null){
            switch (view.getId()){
                case R.id.layout_download_iv:
                    mItemInRvClickListener.onItemWithParamViewClick(view, view.getTag(R.id.dataTag), null);
                    break;
                case R.id.layout_collection_down_iv:
                    mItemInRvClickListener.onItemWithParamPositionClick(view, view.getTag(R.id.dataTag),
                            (int)view.getTag(R.id.dataTag2));
                    break;
            }
        }

    }

    public void setOnItemClickListener(OnItemWithParamViewClickListener listener){
        this.mItemInRvClickListener = listener;
    }

    public static class JokeHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.joke_rv_tv)
        TextView mTvContentText;
        //Collection and Download RelativeLayout
        @Bind(R.id.joke_include)
        RelativeLayout mRlayoutCollectionDownload;
        //Collection ImageView
        @Bind(R.id.layout_collection_down_iv)
        ImageView mIvCollectionDown;
        @Bind(R.id.layout_download_iv)
        ImageView mIvCopy;
        public JokeHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
