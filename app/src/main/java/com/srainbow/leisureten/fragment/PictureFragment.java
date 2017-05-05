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

import com.srainbow.leisureten.netRequest.RetrofitThing;
import com.srainbow.leisureten.netRequest.reWriteWay.SubscriberByTag;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.activity.ContentShowActivity;
import com.srainbow.leisureten.adapter.PictureRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnTVInRvClickToDoListener;
import com.srainbow.leisureten.data.APIData.FunnyPicData;
import com.srainbow.leisureten.data.APIData.FunnyPicDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PictureFragment extends BaseFragment implements SubscriberByTag.onSubscriberByTagListener,
        OnTVInRvClickToDoListener, View.OnClickListener{

    private LinearLayoutManager linearLayoutManager;
    private PictureRVAdapter mPictureRVAdapter;
    private List<FunnyPicDetail> funnyPicDetails;

    @Bind(R.id.picfragment_content_rv)
    RecyclerView mRVFunnyPicture;
    @Bind(R.id.picfragment_srefreshl)
    SwipeRefreshLayout mSRefresh;
    @Bind(R.id.picfragment_load_failed_llayout)
    LinearLayout mLlayoutLoadFailed;

    public PictureFragment(){}

    public static PictureFragment newInstance() {
        return new PictureFragment();
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
        funnyPicDetails = new ArrayList<>();
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
        mPictureRVAdapter = new PictureRVAdapter(getActivity(), funnyPicDetails);
        mPictureRVAdapter.setOnItemClickListener(this);
        mRVFunnyPicture.setAdapter(mPictureRVAdapter);
        mRVFunnyPicture.setLayoutManager(linearLayoutManager);
    }

    public void loadDataByTag(String tag){
        RetrofitThing.getInstance().onFunnyPicResponse(new SubscriberByTag(tag, PictureFragment.this));
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
    public void onTvItemClick(View v) {
        switch (v.getId()){
            case R.id.footer_loadmore_tv:
                //请求数据
                loadDataByTag("loadMore");
                break;
        }
    }

    @Override
    public void onCompleted(String tag) {
        mLlayoutLoadFailed.setVisibility(View.GONE);
        mRVFunnyPicture.setVisibility(View.VISIBLE);
        switch (tag){
            case "init":
                Toast.makeText(getActivity(),"init完成: " + funnyPicDetails.size(), Toast.LENGTH_SHORT).show();
                break;
            case "loadMore":
                Toast.makeText(getActivity(),"loadMore完成: " + funnyPicDetails.size(), Toast.LENGTH_SHORT).show();
                break;
            case "refresh":
                Toast.makeText(getActivity(),"refresh完成: " + funnyPicDetails.size(), Toast.LENGTH_SHORT).show();
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
                try {
                    ((ContentShowActivity)getActivity()).showRefreshFailedPrompt();
                } catch (Exception e1) {
                    Log.e("errorShow", e1.getMessage());
                }
                break;
        }

    }

    @Override
    public void onNext(String tag, Object o) {
        if(o==null||((FunnyPicData)o).result.isEmpty()){
            Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
        }else{
            switch (tag){

                //初始化数据
                case "init":
                    //向funnyPicDetails中填充数据
                    funnyPicDetails.clear();
                    for(FunnyPicDetail detail : ((FunnyPicData)o).result){
                        funnyPicDetails.add(detail);
                    }

                    //刷新RecyclerView数据
                    mPictureRVAdapter.notifyDataSetChanged();
                    break;

                //加载更多
                case "loadMore":
                    //向funnyPicDetails中添加数据
                    for(FunnyPicDetail detail : ((FunnyPicData)o).result){
                        funnyPicDetails.add(detail);
                    }

                    //刷新RecyclerView数据
                    mPictureRVAdapter.notifyDataSetChanged();
                    break;

                //刷新数据
                case "refresh":
                    //获取返回数据集合
                    List<FunnyPicDetail> details = ((FunnyPicData)o).result;
                    for(FunnyPicDetail detail : funnyPicDetails){
                        details.add(detail);
                    }

                    //清空funnyPicDetails
                    funnyPicDetails.clear();

                    //向funnyPicDetails中填充数据
                    for(FunnyPicDetail detail : details){
                        funnyPicDetails.add(detail);
                    }

                    //刷新RecyclerView数据
                    mPictureRVAdapter.notifyDataSetChanged();
                    break;
            }
        }


    }

}
