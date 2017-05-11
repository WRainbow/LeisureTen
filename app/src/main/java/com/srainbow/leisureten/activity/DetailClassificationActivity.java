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
import com.srainbow.leisureten.data.APIData.ShowApiPicClassificationData;
import com.srainbow.leisureten.data.APIData.ShowApiPicClassificationDetail;
import com.srainbow.leisureten.data.APIData.TagDetail;
import com.srainbow.leisureten.fragment.BeatifulCollection.BeautifulCollectionBaseFragment;
import com.srainbow.leisureten.netRequest.RetrofitThing;
import com.srainbow.leisureten.netRequest.reWriteWay.SubscriberByTag;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.HtmlParserWithJSoup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//ShowApi图片显示
public class DetailClassificationActivity extends BaseActivity{

    private static String classification = "";
    private MyViewPagerAdapter myViewPagerAdapter;

    @Bind(R.id.detail_classification_tb)Toolbar mToobar;
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
        showMessageByString("hello");
    }

    public void initParameter(){
        classification = getIntent().getStringExtra("classificationName");
    }

    public void initData(){
        mViewPager.setVisibility(View.GONE);
        RetrofitThing.getInstance().onShowApiPicClassificationResponse(new SubscriberByTag("load",
                new SubscriberByTag.onSubscriberByTagListener() {
            @Override
            public void onCompleted(String tag) {
                mTvLoad.setVisibility(View.GONE);
                mViewPager.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String tag, Throwable e) {
                Log.e("msg", e.getMessage());
            }

            @Override
            public void onNext(String tag, Object o) {
                if(o == null){
                    showMessageByString("没有数据");
                }else{
                    ShowApiPicClassificationDetail detail = ((ShowApiPicClassificationData)o).getShowapi_res_body();
                    initViewPager(detail);
                }
            }
        }));
    }

    public void initViewPager(ShowApiPicClassificationDetail detail){
        for(ShowApiPicClassificationDetail.AllClassification allClassification : detail.getList()){
            if(allClassification.getName().equals(classification)){
                List<ShowApiPicClassificationDetail.Classification> classifications = allClassification.getList();
                for(ShowApiPicClassificationDetail.Classification classification : classifications){
                    mTabLayout.addTab(mTabLayout.newTab().setText(classification.getName()));
                }
            }
        }
        mTabLayout.setupWithViewPager(mViewPager);
        BeautifulCollectionBaseFragment fragment = BeautifulCollectionBaseFragment.newInstance();
    }

    public void initRx(){
        Log.e("msg", "initRx");
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                Log.e("tag", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("tag", "error");
                Log.e("tag", e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                Log.e("tag", "next");
                if(o == null || ((List<TagDetail>)o).isEmpty()){
                    showMessageByString("没有数据");
                }else{
                    List<TagDetail> details = (List<TagDetail>)o;
                    for(TagDetail detail : details){
                        Log.e("tag", detail.getTag() + " " + detail.getUrl());
                    }
                }
            }
        };
        Observable.create(new Observable.OnSubscribe<List<TagDetail>>() {
            @Override
            public void call(Subscriber<? super List<TagDetail>> subscriber) {
                List<TagDetail> list = HtmlParserWithJSoup.getInstance().parserHtmlForTags(Constant.ADDRESS_PICJUMBO);
                if(list.get(0).getTag().equals("error")){
                    try {
                        subscriber.onError(new Exception(list.get(0).getUrl()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                }

            }
        }).subscribeOn(Schedulers.io())
          .unsubscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(subscriber);
    }

}
