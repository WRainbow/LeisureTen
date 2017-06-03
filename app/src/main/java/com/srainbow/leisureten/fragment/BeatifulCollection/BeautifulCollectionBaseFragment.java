package com.srainbow.leisureten.fragment.BeatifulCollection;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.activity.ShowAtlasDetailActivity;
import com.srainbow.leisureten.adapter.BeautifulRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamViewClickListener;
import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureContent;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureQueryResult;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureQueryResultBody;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureSizeUrl;
import com.srainbow.leisureten.fragment.BaseFragment;
import com.srainbow.leisureten.netRequest.BackGroundRequest;
import com.srainbow.leisureten.netRequest.RetrofitThing;
import com.srainbow.leisureten.netRequest.reWriteWay.SubscriberByTag;
import com.srainbow.leisureten.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BeautifulCollectionBaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeautifulCollectionBaseFragment extends BaseFragment implements SubscriberByTag.onSubscriberByTagListener,
        OnItemWithParamViewClickListener, View.OnClickListener, OnResponseListener{

    private static final String ARG_PARAM1 = "param1";
    private String mParam1 = "null";
    private String userName = "";
    //如果加载了再次进入页面时不需要重新加载，使用isLoading进行判断，每次加载完成isLoading设置为false；
    private boolean isLoading = false;
    //同时满足isVisible和isCreateView都为true时在进行加载数据（懒加载模式）;
    private boolean isVisible=false;
    private boolean isCreateView=false;
    private int currentPage = 1;
    private int allPage = 1;
    private View showV, hideV;

    private PictureQueryResultBody resultBody;
    private List<PictureContent> pictureContentList;
    private BeautifulRVAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private View v;

    @Bind(R.id.beautiful_include)
    RelativeLayout mRlayoutPage;
    @Bind(R.id.layout_prepage_tv)
    TextView mTvPrePage;
    @Bind(R.id.layout_nextpage_tv)
    TextView mTvNextPage;
    @Bind(R.id.layout_loading_tv)
    TextView mTvLoading;
    @Bind(R.id.beautiful_base_rv)
    RecyclerView mRecyclerView;

    public BeautifulCollectionBaseFragment() {
        Log.e("BeautifulFragment", "construct");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BeautifulCollectionBaseFragment.
     */
    public static BeautifulCollectionBaseFragment newInstance(String type) {
        BeautifulCollectionBaseFragment fragment = new BeautifulCollectionBaseFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_PARAM1, type);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v != null){
            ViewGroup parent = (ViewGroup)v.getParent();
            if(parent != null){
                parent.removeView(v);
            }
            return v;
        }else {
            v = inflater.inflate(R.layout.fragment_beatiful_collection_base, container, false);
            ButterKnife.bind(this, v);
            initVar();
            initView();
            isCreateView = true;
            lazyLoad();
            return v;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            lazyLoad();
        }
    }

    public void initView(){
        initRv();
        mTvPrePage.setOnClickListener(this);
        mTvNextPage.setOnClickListener(this);
    }

    public void initVar(){
        userName = getUserNameFromSP(getActivity());
    }

    public void initRv(){
        pictureContentList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new BeautifulRVAdapter(getActivity(), pictureContentList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

    }

    public void lazyLoad(){
        if(isCreateView && isVisible) {
            if(!isLoading) {
                RetrofitThing.getInstance().onShowApiPicContentResponse(mParam1, "1", new SubscriberByTag("init", this));
            }
        }
    }

    public void load(String page){
//        isLoading = true;
        showLoadTv();
        RetrofitThing.getInstance().onShowApiPicContentResponse(mParam1, page, new SubscriberByTag("init", this));
    }

    @Override
    public void onCompleted(String tag) {
        mAdapter.notifyDataSetChanged();
        mTvLoading.setVisibility(View.GONE);
        isLoading = true;
        allPage = resultBody.getPagebean().getAllPages();
        currentPage = resultBody.getPagebean().getCurrentPage();
        if(allPage == 0){
            //没有数据，显示提示信息
            mRlayoutPage.setVisibility(View.VISIBLE);
            mTvLoading.setVisibility(View.VISIBLE);
            mTvLoading.setText("没有数据");
            Toast.makeText(getActivity(), "没有数据InFragment", Toast.LENGTH_SHORT).show();
        }else {
            showPageRlayout(allPage, currentPage);
        }
    }

    @Override
    public void onError(String tag, Throwable e) {
        isLoading = false;
    }

    @Override
    public void onNext(String tag, Object o) {
        if(o == null || ((PictureQueryResult)o).getShowapi_res_body() == null){
            Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
        }else{
            switch (tag){
                case "init":
                    pictureContentList.clear();
                    resultBody = ((PictureQueryResult)o).getShowapi_res_body();
                    List<PictureContent> contentList = resultBody.getPagebean().getContentlist();
                    if(contentList.size() > 0) {
                        for (PictureContent content : contentList) {
                            pictureContentList.add(content);
                        }
                    }

                    break;
            }

        }
    }

    @Override
    public void onItemWithParamViewClick(View v, Object o, View anther) {
        //o instanceof List<PictureSizeUrl>, if o != null
        PictureContent pictureContent = (PictureContent)o;

        //show big picture's url
        ArrayList<String> imgUrlList = new ArrayList<>();
        for(PictureSizeUrl pictureSizeUrl : pictureContent.getList()){
            imgUrlList.add(pictureSizeUrl.getBig());
        }

        /*
            if picture collected, information be needed;
            index:0 - typeName like "萌宠"
                  1 - title like "超萌喵星人火爆全场"
                  2 - type like "6002"
                  3 - itemId like "98313694"
                  4 - ct like "2015-12-25 04:11:12.442"
         */
        ArrayList<String> collectionInfo = new ArrayList<>();
        collectionInfo.add(pictureContent.getTypeName());
        collectionInfo.add(pictureContent.getTitle());
        collectionInfo.add(pictureContent.getType());
        collectionInfo.add(pictureContent.getItemId());
        collectionInfo.add(pictureContent.getCt());

        switch (v.getId()){
            //进入图集
            case R.id.beautiful_base_in_llayout:
                Intent intent = new Intent(getActivity(), ShowAtlasDetailActivity.class);
                intent.putStringArrayListExtra("imgUrlList", imgUrlList);
                intent.putStringArrayListExtra("collectionInfo", collectionInfo);
                startActivity(intent);
                Toast.makeText(getActivity(), "进入图集", Toast.LENGTH_SHORT).show();
                break;
            //收藏
            case R.id.layout_collection_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    BackGroundRequest.getInstance().addBeautifulAtlas(this, Constant.ATLAS_COLLECTION_TAG,
                            userName, pictureContent);
                    this.hideV = v;
                    this.showV = anther;

                }
                break;
            //取消收藏
            case R.id.layout_collection_down_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    BackGroundRequest.getInstance().deleteBeautifulAtlas(this, Constant.ATLAS_COLLECTION_CANCEL_TAG,
                            userName, pictureContent.getItemId());
                    this.hideV = v;
                    this.showV = anther;

                }
                break;
            //下载
            case R.id.layout_download_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    showMessageByString("正在下载");
                    if(BackGroundRequest.getInstance().downLoadAtlas(pictureContent)){
                        showMessageByString("下载成功");
                    }else{
                        showMessageByString("下载失败");
                    }

                }
                break;
        }
    }

    @Override
    public void onItemWithParamPositionClick(View v, Object o, int position) {

    }

    public void showPageRlayout(int all, int current){
        mRlayoutPage.setVisibility(View.VISIBLE);
        if(current < all){
            //总页数大于1
            if(current == 1){
                //为第一页
                mTvPrePage.setVisibility(View.GONE);
                mTvNextPage.setVisibility(View.VISIBLE);
            }else{
                //不为第一页
                mTvPrePage.setVisibility(View.VISIBLE);
                mTvNextPage.setVisibility(View.VISIBLE);
            }
        }else if(current == all){
            //第一页或者是最后一页
            if(current == 1){
                //第一页也是最后一页
                showLoadTv();
                mTvLoading.setText("全部数据都在这啦……");
            }else{
                //最后一页
                mTvPrePage.setVisibility(View.VISIBLE);
                mTvNextPage.setVisibility(View.GONE);
            }
        }
    }

    public void showLoadTv(){
        mTvPrePage.setVisibility(View.GONE);
        mTvNextPage.setVisibility(View.GONE);
        mTvLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_prepage_tv:
                load(String.valueOf(currentPage - 1));
                break;
            case R.id.layout_nextpage_tv:
                load(String.valueOf(currentPage + 1));
                break;
        }
    }

    @Override
    public void result(Object object, int tag) {
        if (object != null) {
            try{
                JSONObject result = new JSONObject((String)object);
                switch (tag) {
                    case Constant.ATLAS_COLLECTION_TAG:
                        if ("true".equals(result.optString("result"))) {
                            showMessageByString("收藏成功");
                            showAndHideView(showV, hideV);
                        }  else if ("false".equals(result.optString("result"))) {
                            showMessageByString("收藏失败");
                        } else {
                            showMessageByString("未知错误");
                        }
                        break;
                    case Constant.ATLAS_COLLECTION_CANCEL_TAG:
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showMessageByString("网络错误");
        }
    }
}