package com.srainbow.leisureten.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.AtlasCollectionShowRVAdapter;
import com.srainbow.leisureten.adapter.JokeCollectionShowRVAdapter;
import com.srainbow.leisureten.adapter.PictureCollectionShowRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamViewClickListener;
import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.data.apidata.juhe.joke.JokeDetail;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureContent;
import com.srainbow.leisureten.data.jsoupdata.ImgWithAuthor;
import com.srainbow.leisureten.netRequest.BackGroundRequest;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.JsonToList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowCollectionActivity extends BaseActivity implements OnResponseListener,
        OnItemWithParamViewClickListener {

    private String userName = "";
    private int deletePosition = 0;
    private String collectioinType = "";
    private PictureCollectionShowRVAdapter mPictureCollectionShowRVAdapter;
    private JokeCollectionShowRVAdapter mJokeCollectionShowRVAdapter;
    private AtlasCollectionShowRVAdapter mAtlasCollectionShowRVAdapter;
    private List<ImgWithAuthor> imgWithAuthorList;
    private List<JokeDetail> jokeDetailList;
    private List<PictureContent> pictureContentList;
    private View showV, hideV;

    @Bind(R.id.hdpicture_include)
    Toolbar mToolbar;
    @Bind(R.id.layout_title_tv)
    TextView mTvTitle;
    @Bind(R.id.hdpicture_show_rv)
    RecyclerView mRVShowHDPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdpicture_show);
        ButterKnife.bind(this);
        initVar();
        initTb();
        initView();
        loadData();
    }

    public void initVar() {
        userName = getUserNameFromSP();
        collectioinType = getIntent().getStringExtra("collectionType");
    }

    public void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        switch (collectioinType){
            case "jokeCollection":
                jokeDetailList = new ArrayList<>();
                mJokeCollectionShowRVAdapter = new JokeCollectionShowRVAdapter(this, jokeDetailList);
                mJokeCollectionShowRVAdapter.setOnItemClickListener(this);
                mRVShowHDPicture.setAdapter(mJokeCollectionShowRVAdapter);
                break;
            case "pictureCollection":
                imgWithAuthorList = new ArrayList<>();
                mPictureCollectionShowRVAdapter = new PictureCollectionShowRVAdapter(this, imgWithAuthorList);
                mPictureCollectionShowRVAdapter.setOnItemClickListener(this);
                mRVShowHDPicture.setAdapter(mPictureCollectionShowRVAdapter);
                break;
            case "atlasCollection":
                pictureContentList = new ArrayList<>();
                mAtlasCollectionShowRVAdapter = new AtlasCollectionShowRVAdapter(this, pictureContentList);
                mAtlasCollectionShowRVAdapter.setOnItemClickListener(this);
                mRVShowHDPicture.setAdapter(mAtlasCollectionShowRVAdapter);
                break;
        }
        mRVShowHDPicture.setLayoutManager(linearLayoutManager);
    }

    public void initTb(){
        mTvTitle.setText(collectioinType);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCollectionActivity.this.finish();
            }
        });
    }

    public void loadData() {
        switch (collectioinType) {
            case "jokeCollection":
                BackGroundRequest.getInstance().getJokeCollection(this,
                        Constant.GET_JOKE_COLLECTION, userName);
                break;
            case "pictureCollection":
                BackGroundRequest.getInstance().getPictureCollection(this,
                        Constant.GET_PICTURE_COLLECTION, userName);
                break;
            case "atlasCollection":
                BackGroundRequest.getInstance().getAtlasCollection(this,
                        Constant.GET_ATLAS_COLLECTION, userName);
                break;
        }
    }

    @Override
    public void onItemWithParamViewClick(View v, Object o, View anther) {}

    @Override
    public void onItemWithParamPositionClick(View v, Object o, int position) {
        switch (collectioinType) {
            case "jokeCollection":
                switch (v.getId()) {
                    case R.id.layout_collection_down_iv:
                        BackGroundRequest.getInstance().deleteJoke(this, Constant.JOKE_COLLECTION_CANCEL_TAG,
                                userName, ((JokeDetail)o).getHashId());
                        deletePosition = position;
                        break;
                    case R.id.layout_download_iv:
                        showMessageByString("复制");
                        break;
                }
                break;
            case "pictureCollection":
                switch (v.getId()) {
                    case R.id.layout_collection_down_iv:
                        BackGroundRequest.getInstance().deletePicture(this, Constant.PICTURE_COLLECTION_CANCEL_TAG,
                                userName, ((ImgWithAuthor)o).getImgUrl());
                        deletePosition = position;
                        break;
                    case R.id.layout_download_iv:
                        showMessageByString("下载");
                        break;
                }
                break;
            case "atlasCollection":
                switch (v.getId()) {
                    case R.id.layout_collection_down_iv:
                        BackGroundRequest.getInstance().deleteBeautifulAtlas(this, Constant.ATLAS_COLLECTION_CANCEL_TAG,
                                userName, ((PictureContent)o).getItemId());
                        deletePosition = position;
                        break;
                    case R.id.layout_download_iv:
                        showMessageByString("下载");
                        break;
                }
                break;
        }
    }

    @Override
    public void result(Object object, int tag) {
        if (object != null) {
            try{
                JSONObject result = new JSONObject((String)object);
                switch (collectioinType) {
                    case "jokeCollection":
                        switch (tag) {
                            case Constant.GET_JOKE_COLLECTION:
                                List<JokeDetail> jokeDetails = JsonToList.getInstance().getJokeCollection(result);
                                for (JokeDetail detail : jokeDetails) {
                                    jokeDetailList.add(detail);
                                }
                                mJokeCollectionShowRVAdapter.notifyDataSetChanged();
                                break;
                            case Constant.JOKE_COLLECTION_CANCEL_TAG:
                                if ("true".equals(result.optString("result"))) {
                                    mJokeCollectionShowRVAdapter.notifyItemRemoved(deletePosition);
                                    showMessageByString("已取消收藏");
                                } else if ("false".equals(result.optString("result"))) {
                                    showMessageByString("取消收藏失败");
                                } else {
                                    showMessageByString("未知错误");
                                }
                                break;
                        }
                        break;
                    case "pictureCollection":
                        switch (tag) {
                            case Constant.GET_PICTURE_COLLECTION:
                                List<ImgWithAuthor> imgWithAuthors = JsonToList.getInstance().getAllPictureCollection(result);
                                for (ImgWithAuthor imgWithAuthor : imgWithAuthors) {
                                    imgWithAuthorList.add(imgWithAuthor);
                                }
                                mPictureCollectionShowRVAdapter.notifyDataSetChanged();
                                break;
                            case Constant.PICTURE_COLLECTION_CANCEL_TAG:
                                if ("true".equals(result.optString("result"))) {
                                    mPictureCollectionShowRVAdapter.notifyItemRemoved(deletePosition);
                                    showMessageByString("已取消收藏");
                                } else if ("false".equals(result.optString("result"))) {
                                    showMessageByString("取消收藏失败");
                                } else {
                                    showMessageByString("未知错误");
                                }
                                break;
                        }
                        break;
                    case "atlasCollection":
                        switch (tag) {
                            case Constant.GET_ATLAS_COLLECTION:
                                List<PictureContent> pictureContents = JsonToList.getInstance().getAtlasCollection(result);
                                for (PictureContent pictureContent : pictureContents) {
                                    pictureContentList.add(pictureContent);
                                }
                                mAtlasCollectionShowRVAdapter.notifyDataSetChanged();
                                break;
                            case Constant.ATLAS_COLLECTION_CANCEL_TAG:
                                if ("true".equals(result.optString("result"))) {
                                    mAtlasCollectionShowRVAdapter.notifyItemRemoved(deletePosition);
                                    showMessageByString("已取消收藏");
                                } else if ("false".equals(result.optString("result"))) {
                                    showMessageByString("取消收藏失败");
                                } else {
                                    showMessageByString("未知错误");
                                }
                                break;
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
