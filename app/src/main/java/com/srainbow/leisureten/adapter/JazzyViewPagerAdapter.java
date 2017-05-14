package com.srainbow.leisureten.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;

import com.bumptech.glide.Glide;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.frame.jazzyviewpager.JazzyViewPager;
import com.srainbow.leisureten.frame.jazzyviewpager.OutlineContainer;

import java.util.ArrayList;

/**
 * Created by SRainbow on 2017/5/14.
 */

public class JazzyViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private JazzyViewPager mJazzy;
    private ArrayList<String> imgUrlList;

    public JazzyViewPagerAdapter(Context context, JazzyViewPager jazzyViewPager, ArrayList<String> list){
        this.mContext = context;
        this.mJazzy = jazzyViewPager;
        this.imgUrlList = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_jazzy_adapter, container, false);
        ImageView mIvShowPic = (ImageView)view.findViewById(R.id.layout_pic_iv);
        Glide.with(mContext).load(imgUrlList.get(position)).into(mIvShowPic);
//        mIvShowPic.setPadding(30, 30, 30, 30);
        container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mJazzy.setObjectForPosition(view, position);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object obj) {
        container.removeView(mJazzy.findViewFromObject(position));
    }
    @Override
    public int getCount() {
        return imgUrlList.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object obj) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == obj;
        } else {
            return view == obj;
        }
    }
}
