package com.srainbow.leisureten.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.HDPictureTagRVAdapter;
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

public class MainActivity extends BaseActivity implements View.OnClickListener, OnTVWithUrlInRvClickToDoListener,
        NavigationView.OnNavigationItemSelectedListener{

    @Bind(R.id.main_drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_nav)
    NavigationView mNavigationView;
    @Bind(R.id.main_tb)
    Toolbar mToolbar;

    //drawer header view
    //can find by ButterKnife cause they are attribute of NavigationView
    private ImageView mIvPortrait;
    private TextView mTvUserName;

    @Bind(R.id.main_happy_cv_item1)
    RectangleImageView mHappyRectIvItem1;//默认为趣图
    @Bind(R.id.main_happy_cv_item2)
    RectangleImageView mHappyRectIvItem2;//默认为笑话
    @Bind(R.id.main_other_cv_item1)
    RectangleImageView mOtherRectIvItem1;//默认为生活
    @Bind(R.id.main_other_cv_item2)
    RectangleImageView mOtherRectIvItem2;//默认为娱乐
//    @Bind(R.id.main_other_cv_item3)
//    RectangleImageView mOtherRectIvItem3;//默认为写真
    @Bind(R.id.main_other_cv_item4)
    RectangleImageView mOtherRectIvItem4;//默认为新闻
    @Bind(R.id.main_other_cv_item5)
    RectangleImageView mOtherRectIvItem5;//默认为明星
    @Bind(R.id.main_other_cv_item6)
    RectangleImageView mOtherRectIvItem6;//默认为服装

    @Bind(R.id.main_classification_hdtag_rv)
    RecyclerView mRvHDTag;
    @Bind(R.id.main_classification_hdload_tv)
    TextView mTvLoading;

    private HDPictureTagRVAdapter mHDPictureRvAdapter;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<TagDetail> tagDetailList;

    private String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initVar();
        initViews();
    }

    public void initViews(){
        initToolbar();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mHDPictureRvAdapter = new HDPictureTagRVAdapter(this, tagDetailList);
        mHDPictureRvAdapter.setOnItemClickListener(this);
        mRvHDTag.setAdapter(mHDPictureRvAdapter);
        mRvHDTag.setLayoutManager(staggeredGridLayoutManager);

        mHappyRectIvItem1.setOnClickListener(this);
        mHappyRectIvItem2.setOnClickListener(this);
        mOtherRectIvItem1.setOnClickListener(this);
        mOtherRectIvItem2.setOnClickListener(this);
//        mOtherRectIvItem3.setOnClickListener(this);
        mOtherRectIvItem4.setOnClickListener(this);
        mOtherRectIvItem5.setOnClickListener(this);
        mOtherRectIvItem6.setOnClickListener(this);

        //find NavigationView's headerLayout
        View headerView = mNavigationView.getHeaderView(0);
        mIvPortrait = (ImageView)headerView.findViewById(R.id.nav_header_iv);
        mTvUserName = (TextView)headerView.findViewById(R.id.nav_header_username_tv);

        mNavigationView.setNavigationItemSelectedListener(this);
        mTvUserName.setOnClickListener(this);
        mIvPortrait.setOnClickListener(this);

    }

    public void initToolbar(){
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

    public void initRx(){
        Observable.create(new Observable.OnSubscribe<List<TagDetail>>(){

            @Override
            public void call(Subscriber<? super List<TagDetail>> subscriber){

            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TagDetail>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<TagDetail> tagDetails) {

                    }
                });
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
//            case R.id.main_other_cv_item3:
//                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
//                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem3.getImgText()));
//                startActivity(intent);
//                break;
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
            //portrait clicked
            case R.id.nav_header_iv:
                showMessageByString("Portrait");
                startActivityForResult(new Intent(MainActivity.this, ShowAtlasDetailActivity.class), 1);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            //userName clicked
            case R.id.nav_header_username_tv:
                showMessageByString("UserName");
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            default:

        }
    }

    @Override
    public void onTvItemClick(View v, String url) {
        Log.e("onTvItemClick", url);
        Intent intent = new Intent(MainActivity.this, HDPictureShowActivity.class);
        intent.putExtra("tagUrl", url);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu_about:
                showMessageByString("关于");
                break;
            case R.id.nav_menu_collection:
                showMessageByString("收藏");
                break;
            case R.id.nav_menu_feedback:
                showMessageByString("反馈");
                break;
            case R.id.nav_menu_music:
                showMessageByString("音乐");
                break;
            case R.id.nav_menu_setting:
                showMessageByString("设置");
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    if(getIntent().getExtras().getString("userName") != null){
                        userName = getIntent().getExtras().getString("userName");
                        mTvUserName.setText(userName);
                    }
                }
                break;
        }
    }

    public String getCodeString(String showString){
        switch (showString){
            case "生活":
                return "生活趣味";
            case "娱乐":
                return "娱乐八卦";
            case "写真":
                return "美女图片";
            case "新闻":
                return "社会百态";
            case "明星":
                return "明星写真";
            case "服装":
                return "时尚伊人";
        }
        return null;
    }
}
