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
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamViewClickListener;
import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.data.APIData.ImgWithAuthor;
import com.srainbow.leisureten.netRequest.BackGroundRequest;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.HtmlParserWithJSoup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HDPictureShowActivity extends BaseActivity implements OnClickListener,
        OnItemWithParamViewClickListener, OnResponseListener {

    private String userName = "";
    private String URL = "", TAG = "";

    private HDPictureShowRVAdapter mHDPictureShowRVAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ImgWithAuthor> imgWithAuthorList;
    private String prePageUrl = "", nextPageUrl = "";
    private View showV, hideV;

    @Bind(R.id.hdpicture_include)
    Toolbar mToolbar;
    @Bind(R.id.layout_title_tv)
    TextView mTvTitle;
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
        initVars();
        initViews();
        rxJava();
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
        initTb();
    }

    public void initTb(){
        mTvTitle.setText(TAG);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HDPictureShowActivity.this.finish();
            }
        });
    }

    public void initVars(){
        userName = getUserNameFromSP();
        URL = getIntent().getStringExtra("tagUrl");
        TAG = getIntent().getStringExtra("tag");
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
    public void onItemWithParamViewClick(View v, Object o, View anther) {
        switch (v.getId()){
            //Collection ImageView clickListener
            case R.id.layout_collection_iv:
                if ("null".equals(userName)) {
                   showMessageByString("请先登录");
                } else {
                    BackGroundRequest.getInstance().addHDPicture(this, Constant.PICTURE_COLLECTION_TAG,
                            userName, (ImgWithAuthor)o);
                    this.hideV = v;
                    this.showV = anther;

                }
                break;
            //cancel collection imageView clicked
            case R.id.layout_collection_down_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    BackGroundRequest.getInstance().deletePicture(this, Constant.PICTURE_COLLECTION_CANCEL_TAG,
                            userName, ((ImgWithAuthor)o).getImgUrl());
                    this.hideV = v;
                    this.showV = anther;
                }
                break;
            //Download ImageView clickListener;
            case R.id.layout_download_iv:
                if ("null".equals(userName)) {
                   showMessageByString("请先登录");
                } else {
                    showMessageByString("正在下载");
                    if(BackGroundRequest.getInstance().downLoadImage(((ImgWithAuthor)o).getImgUrl())){
                        showMessageByString("下载成功");
                    }else{
                        showMessageByString("下载失败");
                    }

                }
                break;
        }
    }

    @Override
    public void result(JSONObject result, int tag) {
        if (result != null) {
            switch (tag) {
                case Constant.PICTURE_COLLECTION_TAG:
                    if ("true".equals(result.optString("result"))) {
                        showMessageByString("收藏成功");
                        showAndHideView(showV, hideV);
                    } else if ("false".equals(result.optString("result"))) {
                        showMessageByString("收藏失败");
                    } else {
                        showMessageByString("未知错误");
                    }
                    break;
                case Constant.PICTURE_COLLECTION_CANCEL_TAG:
                    if ("true".equals(result.optString("result"))) {
                        showMessageByString("取消收藏成功");
                        showAndHideView(showV, hideV);
                    }  else if ("false".equals(result.optString("result"))) {
                        showMessageByString("取消收藏失败");
                    } else {
                        showMessageByString("未知错误");
                    }
                    break;
            }
        } else {
            showMessageByString("网络错误");
        }
    }
}
