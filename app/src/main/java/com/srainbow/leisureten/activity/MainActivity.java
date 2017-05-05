package com.srainbow.leisureten.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.HDPictureRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnTVWithUrlInRvClickToDoListener;
import com.srainbow.leisureten.data.APIData.TagDetail;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.HtmlParserWithJSoup;
import com.srainbow.leisureten.widget.RectangleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnTVWithUrlInRvClickToDoListener{

    @Bind(R.id.main_happy_cv_item1)
    RectangleImageView mHappyRectIvItem1;//默认为趣图
    @Bind(R.id.main_happy_cv_item2)
    RectangleImageView mHappyRectIvItem2;//默认为笑话
    @Bind(R.id.main_other_cv_item1)
    RectangleImageView mOtherRectIvItem1;//默认为动物
    @Bind(R.id.main_other_cv_item2)
    RectangleImageView mOtherRectIvItem2;//默认为美食
    @Bind(R.id.main_other_cv_item3)
    RectangleImageView mOtherRectIvItem3;//默认为婚纱
    @Bind(R.id.main_other_cv_item4)
    RectangleImageView mOtherRectIvItem4;//默认为社会
    @Bind(R.id.main_other_cv_item5)
    RectangleImageView mOtherRectIvItem5;//默认为猎奇
    @Bind(R.id.main_other_cv_item6)
    RectangleImageView mOtherRectIvItem6;//默认为服装

    @Bind(R.id.main_classification_hdtag_rv)
    RecyclerView mRvHDTag;
    @Bind(R.id.main_classification_hdload_tv)
    TextView mTvLoading;

    private HDPictureRVAdapter mHDPictureRvAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<TagDetail> tagDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        initVar();
        initViews();
    }

    public void initViews(){
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(6, 5);
        mHDPictureRvAdapter = new HDPictureRVAdapter(this, tagDetailList);
        mHDPictureRvAdapter.setOnItemClickListener(this);
        mRvHDTag.setAdapter(mHDPictureRvAdapter);
        mRvHDTag.setLayoutManager(staggeredGridLayoutManager);

        mHappyRectIvItem1.setOnClickListener(this);
        mHappyRectIvItem2.setOnClickListener(this);
        mOtherRectIvItem1.setOnClickListener(this);
        mOtherRectIvItem2.setOnClickListener(this);
        mOtherRectIvItem3.setOnClickListener(this);
        mOtherRectIvItem4.setOnClickListener(this);
        mOtherRectIvItem5.setOnClickListener(this);
        mOtherRectIvItem6.setOnClickListener(this);
    }

    public void initVar(){
        tagDetailList = new ArrayList<>();
        rxJava();

    }

    public void rxJava(){
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                Log.e("tag", "completed");
                mTvLoading.setVisibility(View.GONE);
                mHDPictureRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("tag", e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                if(o == null || ((List<TagDetail>)o).isEmpty()){
                    showMessageByString("没有数据");
                }else{
                    List<TagDetail> details = (List<TagDetail>)o;
                    tagDetailList.clear();
                    for(TagDetail detail : details){
                        tagDetailList.add(detail);
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

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.main_happy_cv_item1:
                intent = new Intent(MainActivity.this, ContentShowActivity.class);
                intent.putExtra("classificationName", mHappyRectIvItem1.getImgText());
                startActivity(intent);
                break;
            case R.id.main_happy_cv_item2:
                intent = new Intent(MainActivity.this, ContentShowActivity.class);
                intent.putExtra("classificationName", mHappyRectIvItem2.getImgText());
                startActivity(intent);
                break;
            case R.id.main_other_cv_item1:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem1.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item2:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem2.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item3:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem3.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item4:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem4.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item5:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem5.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item6:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem6.getImgText()));
                startActivity(intent);
                break;
            default:

        }
    }

    public String getCodeString(String showString){
        switch (showString){
            case "生活":
                return "生活趣味";
            case "娱乐":
                return "娱乐八卦";
            case "写真":
                return "美女写真";
            case "新闻":
                return "社会百态";
            case "明星":
                return "明星写真";
            case "服装":
                return "时尚伊人";
        }
        return null;
    }

    @Override
    public void onTvItemClick(View v, String url) {

    }
}
