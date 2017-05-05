package com.srainbow.leisureten.custom.Listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by SRainbow on 2017/4/9.
 */

public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager mLinearLayoutManager;
    private RecyclerView.Adapter mAdapter;

    //当前页，初始为0
    private int currentPage = 0;

    //已经加载出来的Item数量
    private int totalItemCount;

    //上一次的totalItemCount
    private int previousTotal;

    //在屏幕中可见的Item数量
    private int visibleItemCount;

    //在屏幕中可见的第一个Item
    private int firstVisibleItem;

    //在屏幕中可见的最后一个Item
    private int lastVisibleItem;

    //是否正在加载数据
    private boolean isLoading = true;

    protected EndlessOnScrollListener(LinearLayoutManager linearLayoutManager, RecyclerView.Adapter adapter){
        this.mLinearLayoutManager = linearLayoutManager;
        mAdapter = adapter;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState == RecyclerView.SCROLL_STATE_IDLE &&
                lastVisibleItem + 1 == mAdapter.getItemCount()){
            currentPage ++;
            onLoadMore(currentPage);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
    }

    public abstract void onLoadMore(int currentPage);
}
