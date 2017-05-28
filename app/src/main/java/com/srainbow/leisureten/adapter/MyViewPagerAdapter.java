package com.srainbow.leisureten.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.srainbow.leisureten.data.apidata.showapi.picture_classification.ClassificationDetail;
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
            String id = s.getId();
            if(!id.equals("6001") && !id.equals("6003") && !id.equals("3001")
                    && !id.equals("3002") && !id.equals("5001")
                    && !id.equals("5002") && !id.equals("5004"))
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
