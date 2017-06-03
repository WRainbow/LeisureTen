package com.srainbow.leisureten.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.JazzyViewPagerAdapter;
import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.frame.jazzyviewpager.JazzyViewPager;
import com.srainbow.leisureten.netRequest.BackGroundRequest;
import com.srainbow.leisureten.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowAtlasDetailActivity extends BaseActivity implements View.OnClickListener,
        OnResponseListener {

    @Bind(R.id.atlas_detail_jvp)
    JazzyViewPager mJazzy;
    @Bind(R.id.atlas_detail_tb_include)
    Toolbar mToolbar;
    @Bind(R.id.layout_title_tv)
    TextView mTvTitle;
    @Bind(R.id.atlas_detail_include)
    RelativeLayout mRlayoutCollectionDownload;
    @Bind(R.id.layout_collection_iv)
    ImageView mIvCollection;
    @Bind(R.id.layout_collection_down_iv)
    ImageView mIvCollectionDown;
    @Bind(R.id.layout_download_iv)
    ImageView mIvDownload;
    @Bind(R.id.atlas_detail_pagenum_tv)
    TextView mTvPageNum;

    private String userName = "";
    private JazzyViewPagerAdapter mJazzyAdapter;
    private ArrayList<String> imgUrlList, collectionInfo;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_atlas_detail);
        ButterKnife.bind(this);
        initVar();
        initView();
    }

    public void initVar(){
        userName = getUserNameFromSP();
        imgUrlList = getIntent().getStringArrayListExtra("imgUrlList");
        collectionInfo = getIntent().getStringArrayListExtra("collectionInfo");
    }

    public void initView(){
        mTvPageNum.setText(String.format(getResources().getString(R.string.pageNum),
                1, imgUrlList.size()));
        mTvTitle.setText(collectionInfo.get(1));
        initVp();
        initTb();
        mIvCollection.setOnClickListener(this);
        mIvDownload.setOnClickListener(this);
        mIvCollectionDown.setOnClickListener(this);
    }

    public void initTb(){
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAtlasDetailActivity.this.finish();
            }
        });
    }

    public void initVp(){
        mJazzyAdapter = new JazzyViewPagerAdapter(this, mJazzy, imgUrlList);
        mJazzy.setAdapter(mJazzyAdapter);
        mJazzy.setOffscreenPageLimit(5);
        mJazzy.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                mTvPageNum.setText(String.format(getResources().getString(R.string.pageNum),
                        position + 1, imgUrlList.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //Collection ImageView Clicked
            case R.id.layout_collection_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    BackGroundRequest.getInstance().addBeautifulPicture(this, Constant.PICTURE_COLLECTION_TAG,
                            userName, imgUrlList.get(currentPage), collectionInfo);
                }
                break;
            //Collection cancel ImageView clicked
            case R.id.layout_collection_down_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    BackGroundRequest.getInstance().deletePicture(this, Constant.PICTURE_COLLECTION_CANCEL_TAG,
                            userName, imgUrlList.get(currentPage));

                }
                break;
            //Download ImageView Clicked
            case R.id.layout_download_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    showMessageByString("正在下载");
                    if(BackGroundRequest.getInstance().downLoadImage(imgUrlList.get(currentPage))){
                        showMessageByString("下载成功");
                    }else{
                        showMessageByString("下载失败");
                    }

                }
                break;
        }
    }

    @Override
    public void result(Object object, int tag) {
        if (object != null) {
            try{
                JSONObject result = new JSONObject((String)object);
                switch (tag) {
                    case Constant.PICTURE_COLLECTION_TAG:
                        if ("true".equals(result.optString("result"))) {
                            showMessageByString("收藏成功");
                            showAndHideView(mIvCollectionDown, mIvCollection);
                        }  else if ("false".equals(result.optString("result"))) {
                            showMessageByString("收藏失败");
                        } else {
                            showMessageByString("未知错误");

                        }
                        break;
                    case Constant.PICTURE_COLLECTION_CANCEL_TAG:
                        if ("true".equals(result.optString("result"))) {
                            showMessageByString("取消收藏成功");
                            showAndHideView(mIvCollection, mIvCollectionDown);
                        }  else if ("false".equals(result.optString("result"))) {
                            showMessageByString("取消收藏失败");
                        } else {
                            showMessageByString("未知错误");

                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showMessageByString("网络错误");
        }
    }
}
