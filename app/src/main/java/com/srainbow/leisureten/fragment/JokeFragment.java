package com.srainbow.leisureten.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.srainbow.leisureten.custom.interfaces.OnItemWithParamViewClickListener;
import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.data.apidata.juhe.joke.RandomJokeData;
import com.srainbow.leisureten.netRequest.BackGroundRequest;
import com.srainbow.leisureten.netRequest.RetrofitThing;
import com.srainbow.leisureten.netRequest.reWriteWay.SubscriberByTag;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.activity.ContentShowActivity;
import com.srainbow.leisureten.adapter.JokeRVAdapter;
import com.srainbow.leisureten.data.apidata.juhe.joke.JokeData;
import com.srainbow.leisureten.data.apidata.juhe.joke.JokeDetail;
import com.srainbow.leisureten.util.Constant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class JokeFragment extends BaseFragment implements SubscriberByTag.onSubscriberByTagListener,
        OnItemWithParamViewClickListener, View.OnClickListener, OnResponseListener{

    private String userName = "";
    private int page = 1;
    private LinearLayoutManager linearLayoutManager;
    private JokeRVAdapter mJokeRVAdapter;
    private List<JokeDetail> jokeDetails;
    private View showV, hideV;

    @Bind(R.id.picfragment_content_rv)
    RecyclerView mRVFunnyPicture;
    @Bind(R.id.picfragment_srefreshl)
    SwipeRefreshLayout mSRefresh;
    @Bind(R.id.picfragment_load_failed_llayout)
    LinearLayout mLlayoutLoadFailed;

    public JokeFragment() {}

    public static JokeFragment newInstance(){
        return new JokeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, view);
        initVars();
        initViews();
        return view;
    }

    public void initVars(){
        userName = getUserNameFromSP(getActivity());
        jokeDetails = new ArrayList<>();
        loadDataByTag("init");
    }

    public void initViews(){
        initRecyclerView();
        initSRefresh();
        mLlayoutLoadFailed.setOnClickListener(this);
    }

    public void initSRefresh(){
        mSRefresh.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark700));
        mSRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataByTag("refresh");
            }
        });
    }

    public void initRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mJokeRVAdapter = new JokeRVAdapter(getActivity(), jokeDetails);
        mJokeRVAdapter.setOnItemClickListener(this);
        mRVFunnyPicture.setAdapter(mJokeRVAdapter);
        mRVFunnyPicture.setLayoutManager(linearLayoutManager);
    }

    public void loadDataByTag(String tag){
        switch (tag) {
            case "init":
                RetrofitThing.getInstance().onJokeResponse("desc", page, 10, String.valueOf(new Date().getTime() / 1000),
                        new SubscriberByTag(tag, JokeFragment.this));
                break;
            case "loadMore":
                page ++;
                RetrofitThing.getInstance().onJokeResponse("desc", page, 10, String.valueOf(new Date().getTime() / 1000),
                        new SubscriberByTag(tag, JokeFragment.this));
                break;
            case "refresh":
                RetrofitThing.getInstance().onRandomJokeResponse(new SubscriberByTag(tag, JokeFragment.this));
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.picfragment_load_failed_llayout:
                loadDataByTag("init");
                break;
        }
    }

    @Override
    public void onItemWithParamViewClick(View v, Object o, View anther) {
        switch (v.getId()){
            case R.id.footer_loadmore_tv:
                //请求数据
                loadDataByTag("loadMore");
                break;
            //Collection ImageView Clicked
            case R.id.layout_collection_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    BackGroundRequest.getInstance().addJoke(this, Constant.JOKE_COLLECTION_TAG, userName, (JokeDetail)o);
                    this.hideV = v;
                    this.showV = anther;

                }

                break;
            //cancel collection imageView clicked
            case R.id.layout_collection_down_iv:
                if ("null".equals(userName)) {
                    showMessageByString("请先登录");
                } else {
                    BackGroundRequest.getInstance().deleteJoke(this, Constant.JOKE_COLLECTION_CANCEL_TAG,
                            userName, ((JokeDetail)o).getHashId());
                    this.hideV = v;
                    this.showV = anther;

                }
                break;

            //Copy ImageView Clicked;
            case R.id.layout_download_iv:
                Toast.makeText(getActivity(), "复制成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void result(JSONObject result, int tag) {
        if (result != null) {
            switch (tag) {
                case Constant.JOKE_COLLECTION_TAG:
                    if ("true".equals(result.optString("result"))) {
                       showMessageByString("收藏成功");
                        showAndHideView(showV, hideV);
                    } else if ("false".equals(result.optString("result"))) {
                        showMessageByString("收藏失败");
                    } else {
                        showMessageByString("未知错误");
                    }
                    break;
                case Constant.JOKE_COLLECTION_CANCEL_TAG:
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

    @Override
    public void onCompleted(String tag) {
        mLlayoutLoadFailed.setVisibility(View.GONE);
        mRVFunnyPicture.setVisibility(View.VISIBLE);
        switch (tag){
            case "init":
//                Toast.makeText(getActivity(),"init完成: " + jokeDetails.size(), Toast.LENGTH_SHORT).show();
                break;
            case "loadMore":
                showMessageByString("加载完成");
                break;
            case "refresh":
                showMessageByString("刷新完成");
                mSRefresh.setRefreshing(false);
                break;
        }
    }

    @Override
    public void onError(String tag, Throwable e) {
        Log.e(tag, e.getMessage());
        switch (tag){
            case "init":
                mRVFunnyPicture.setVisibility(View.GONE);
                mLlayoutLoadFailed.setVisibility(View.VISIBLE);
                break;
            case "loadMore":
                ((ContentShowActivity)getActivity()).showLoadMoreFailedPrompt();
                break;
            case "refresh":
                mSRefresh.setRefreshing(false);
                ((ContentShowActivity)getActivity()).showRefreshFailedPrompt();
                break;
        }

    }

    @Override
    public void onNext(String tag, Object o) {
        if(o==null||((JokeData)o).result.data.isEmpty()){
            Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
        }else{
            switch (tag){

                //初始化数据
                case "init":
                    //向funnyPicDetails中填充数据
                    jokeDetails.clear();
                    for(JokeDetail detail : ((JokeData)o).result.data){
                        jokeDetails.add(detail);
                    }

                    //刷新RecyclerView数据
                    mJokeRVAdapter.notifyDataSetChanged();
                    break;

                //加载更多
                case "loadMore":
                    //向funnyPicDetails中添加数据
                    for(JokeDetail detail : ((JokeData)o).result.data){
                        jokeDetails.add(detail);
                    }

                    //刷新RecyclerView数据
                    mJokeRVAdapter.notifyDataSetChanged();
                    break;

                //刷新数据
                case "refresh":
                    //获取返回数据集合
                    List<JokeDetail> details = ((RandomJokeData)o).result;
                    for(JokeDetail detail : jokeDetails){
                        details.add(detail);
                    }

                    //清空funnyPicDetails
                    jokeDetails.clear();

                    //向funnyPicDetails中填充数据
                    for(JokeDetail detail : details){
                        jokeDetails.add(detail);
                    }

                    //刷新RecyclerView数据
                    mJokeRVAdapter.notifyDataSetChanged();
                    break;
            }
        }


    }

}
