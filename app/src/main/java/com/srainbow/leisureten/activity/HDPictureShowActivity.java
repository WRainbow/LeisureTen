package com.srainbow.leisureten.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.HDPictureShowRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.data.APIData.ImgWithAuthor;
import com.srainbow.leisureten.util.HtmlParserWithJSoup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HDPictureShowActivity extends BaseActivity implements OnClickListener, OnItemWithParamClickListener{

    private static  String URL = "";

    private HDPictureShowRVAdapter mHDPictureShowRVAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ImgWithAuthor> imgWithAuthorList;
    private String prePageUrl = "", nextPageUrl = "";

    @Bind(R.id.hdpicture_show_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.hdpicture_show_rv)
    RecyclerView mRVShowHDPicture;
    @Bind(R.id.hdpicture_page_include)
    RelativeLayout mRLayoutPage;
    @Bind(R.id.layout_prepage_tv)
    TextView mTvPrePage;
    @Bind(R.id.layout_nextpage_tv)
    TextView mTvNextPage;
    @Bind(R.id.layout_loading_tv)
    TextView mTvLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdpicture_show);
        ButterKnife.bind(this);
        initViews();
        initVars();
    }

    public void initViews(){
        imgWithAuthorList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        mHDPictureShowRVAdapter = new HDPictureShowRVAdapter(this, imgWithAuthorList);
        mHDPictureShowRVAdapter.setOnItemClickListener(this);
        mRVShowHDPicture.setAdapter(mHDPictureShowRVAdapter);
        mRVShowHDPicture.setLayoutManager(linearLayoutManager);

        mTvPrePage.setOnClickListener(this);
        mTvNextPage.setOnClickListener(this);
    }

    public void initVars(){
        URL = getIntent().getStringExtra("tagUrl");
        Log.e("getUrl", URL);
        rxJava();
    }

    public void rxJava(){
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                Log.e("tag", "completed");
                mHDPictureShowRVAdapter.notifyDataSetChanged();
                isShowPageTv();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("tag", e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                if(o == null || ((List<ImgWithAuthor>)o).isEmpty()){
                    showMessageByString("没有数据");
                }else{
                    List<ImgWithAuthor> details = (List<ImgWithAuthor>)o;
                    imgWithAuthorList.clear();
                    for(ImgWithAuthor detail : details){
                        imgWithAuthorList.add(detail);
                    }
                }
            }
        };
        Observable.create(new Observable.OnSubscribe<List<ImgWithAuthor>>() {
            @Override
            public void call(Subscriber<? super List<ImgWithAuthor>> subscriber) {
                List<ImgWithAuthor> list = HtmlParserWithJSoup.getInstance().parserHtmlForImgWithAuthor(URL);
                if(list.size() == 0){
                    try {
                        subscriber.onError(new Exception(list.get(0).getImgUrl()));

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

    public void isShowPageTv(){
        mRLayoutPage.setVisibility(View.VISIBLE);
        mTvLoading.setVisibility(View.GONE);
        if(imgWithAuthorList.get(0).getPrePage() == 1){
            mTvPrePage.setVisibility(View.VISIBLE);
            prePageUrl = imgWithAuthorList.get(0).getPreUrl();
        }else if(imgWithAuthorList.get(0).getPrePage() == 0){
            mTvPrePage.setVisibility(View.GONE);
        }
        if(imgWithAuthorList.get(0).getNextPage() == 1){
            mTvNextPage.setVisibility(View.VISIBLE);
            nextPageUrl = imgWithAuthorList.get(0).getNextUrl();
        }else if(imgWithAuthorList.get(0).getNextPage() == 0){
            mTvNextPage.setVisibility(View.GONE);
        }
    }

    public void showPageTv(){
        mTvPrePage.setVisibility(View.VISIBLE);
        mTvNextPage.setVisibility(View.VISIBLE);
        mTvLoading.setVisibility(View.GONE);
    }

    public void showLoadTv(){
        mTvPrePage.setVisibility(View.GONE);
        mTvNextPage.setVisibility(View.GONE);
        mTvLoading.setVisibility(View.VISIBLE);
    }

    public void load(){
        showLoadTv();
        rxJava();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_prepage_tv:
                //由于URL需要在Observabler中使用，如果用局部变量代替时需要定义局部变量为final，因此用改变URL的值来实现上下翻页
                URL = prePageUrl;
                load();
                break;
            case R.id.layout_nextpage_tv:
                URL = nextPageUrl;
                load();
                break;
        }
    }

    @Override
    public void onItemWithParamClick(View v, Object o) {
        switch (v.getId()){
            //Collection ImageView clickListener
            case R.id.layout_collection_iv:
                showMessageByString("Collection");
                break;
            //Download ImageView clickListener;
            case R.id.layout_download_iv:
                showMessageByString("Download");
                break;
        }
    }
}
