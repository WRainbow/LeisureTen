package com.srainbow.leisureten.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by SRainbow on 2017/5/11.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private Fragment fragment;
    private int pageSize = 0;

    public MyViewPagerAdapter(FragmentManager fm, Fragment fragment, int size) {
        super(fm);
        this.fragment = fragment;
        this.pageSize = size;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment;
    }

    @Override
    public int getCount() {
        return pageSize;
    }
}
