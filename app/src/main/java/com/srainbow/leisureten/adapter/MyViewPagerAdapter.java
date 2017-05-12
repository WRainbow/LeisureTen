package com.srainbow.leisureten.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.srainbow.leisureten.data.APIData.showapi.picture_classification.ClassificationDetail;
import com.srainbow.leisureten.fragment.BeatifulCollection.BeautifulCollectionBaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SRainbow on 2017/5/11.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<BeautifulCollectionBaseFragment> fragmentList;
    private List<ClassificationDetail> mClassificationDetail;

    public MyViewPagerAdapter(FragmentManager fm, List<ClassificationDetail> list) {
        super(fm);
        fragmentList = new ArrayList<>();
        mClassificationDetail = list;
        for(ClassificationDetail s : list){
            fragmentList.add(BeautifulCollectionBaseFragment.newInstance(s.getId()));
        }
        Log.e("adapter", fragmentList.size() + "");
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.e("adapter", mClassificationDetail.get(position).getName());
        return mClassificationDetail.get(position).getName();
    }
}
