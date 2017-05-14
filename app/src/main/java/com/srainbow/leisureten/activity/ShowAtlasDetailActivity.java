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
import com.srainbow.leisureten.frame.jazzyviewpager.JazzyViewPager;
import com.srainbow.leisureten.netRequest.BackGroundRequest;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowAtlasDetailActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.atlas_detail_jvp)
    JazzyViewPager mJazzy;
    @Bind(R.id.atlas_detail_tb)
    Toolbar mToolbar;
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
        imgUrlList = getIntent().getStringArrayListExtra("imgUrlList");
        collectionInfo = getIntent().getStringArrayListExtra("collectionInfo");
    }

    public void initView(){
        mTvPageNum.setText(String.format(getResources().getString(R.string.pageNum),
                1, imgUrlList.size()));
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
                if(BackGroundRequest.getInstance().addBeautifulPicture(
                        imgUrlList.get(currentPage), collectionInfo)){
                    showMessageByString("已收藏");
                }
                break;
            //Download ImageView Clicked
            case R.id.layout_download_iv:
                break;
        }
    }
}
