package com.srainbow.leisureten.activity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.HDPictureShowRVAdapter;
import com.srainbow.leisureten.data.APIData.ImgWithAuthor;
import com.srainbow.leisureten.data.APIData.TagDetail;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.HtmlParserWithJSoup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HDPictureShowActivity extends BaseActivity{

    private static  String URL = "";

    private HDPictureShowRVAdapter mHDPictureShowRVAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ImgWithAuthor> imgWithAuthorList;

    @Bind(R.id.hdpicture_show_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.hdpicture_show_rv)
    RecyclerView mRVShowHDPicture;
    @Bind(R.id.testiv)
    ImageView mIVTest;

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
        mRVShowHDPicture.setAdapter(mHDPictureShowRVAdapter);
        mRVShowHDPicture.setLayoutManager(linearLayoutManager);
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
                Glide.with(HDPictureShowActivity.this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1494103026105&di=9dd40af4525dfcbcda70eecedafe8ce6&imgtype=0&src=http%3A%2F%2Fimage.unjeep.com%2Fupload%2Fa%2F46%2Fa46fbca024db647b1953fcbfa98276e7.png").into(mIVTest);
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
                        Log.e("imgUrl", detail.getImgUrl());
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
}
