package com.srainbow.leisureten.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.MyViewPagerAdapter;
import com.srainbow.leisureten.data.apidata.showapi.picture_classification.Classification;
import com.srainbow.leisureten.data.apidata.showapi.picture_classification.ClassificationDetail;
import com.srainbow.leisureten.data.apidata.showapi.picture_classification.PictureClassificationResult;
import com.srainbow.leisureten.data.apidata.showapi.picture_classification.PictureClassificationResultBody;
import com.srainbow.leisureten.netRequest.RetrofitThing;
import com.srainbow.leisureten.netRequest.reWriteWay.SubscriberByTag;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//ShowApi图片显示
public class DetailClassificationActivity extends BaseActivity{

    private String classificationName = "";
    private List<ClassificationDetail> mClassificationId;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Bind(R.id.detail_classification_include)Toolbar mToobar;
    @Bind(R.id.layout_title_tv)TextView mTvTitle;
    @Bind(R.id.detail_classification_tlayout)TabLayout mTabLayout;
    @Bind(R.id.detail_classification_vp)ViewPager mViewPager;
    @Bind(R.id.detail_classification_load_tv)TextView mTvLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_classification);
        ButterKnife.bind(this);
        initParameter();
        initData();
        initView();
    }

    public void initParameter(){
        classificationName = getIntent().getStringExtra("classificationName");
        mClassificationId = new ArrayList<>();
    }

    public void initView(){
        initTb();
    }

    public void initTb(){
        mTvTitle.setText(classificationName);
        mToobar.setNavigationIcon(R.drawable.ic_back);
        mToobar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailClassificationActivity.this.finish();
            }
        });
    }

    public void initData(){
        RetrofitThing.getInstance().onShowApiPicClassificationResponse(new SubscriberByTag("load",
                new SubscriberByTag.onSubscriberByTagListener() {
            @Override
            public void onCompleted(String tag) {
                mTvLoad.setVisibility(View.GONE);
                showMessageByString("load done");
            }

            @Override
            public void onError(String tag, Throwable e) {
                Log.e("DetailActivityError", e.getMessage());
            }

            @Override
            public void onNext(String tag, Object o) {
                if(o == null){
                    showMessageByString("没有数据");
                }else{
                    PictureClassificationResultBody resultBody = ((PictureClassificationResult)o).getShowapi_res_body();
                    initViewPager(resultBody);
                }
            }
        }));
    }

    public void initViewPager(PictureClassificationResultBody detail){

        for(Classification classification : detail.getList()){
            if(classification.getName().equals(classificationName)){
                mClassificationId = classification.getList();
                break;
            }
        }

        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mClassificationId);
        mViewPager.setAdapter(myViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
