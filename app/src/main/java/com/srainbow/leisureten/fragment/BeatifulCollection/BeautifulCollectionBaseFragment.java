package com.srainbow.leisureten.fragment.BeatifulCollection;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.BeautifulRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureContent;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureQueryResult;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureQueryResultBody;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureSizeUrl;
import com.srainbow.leisureten.fragment.BaseFragment;
import com.srainbow.leisureten.netRequest.RetrofitThing;
import com.srainbow.leisureten.netRequest.reWriteWay.SubscriberByTag;

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
        OnItemWithParamClickListener {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1 = "null";
    private boolean isVisible=false;
    private boolean isCreateView=false;

    private List<PictureContent> imgListWithDescriptionList;
    private BeautifulRVAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Bind(R.id.test)
    TextView mTvTest;
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
        Log.e("BeautifulFragment", "newInstance");
        BeautifulCollectionBaseFragment fragment = new BeautifulCollectionBaseFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG_PARAM1, type);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("BeautifulFragment", mParam1 + "onCreate");
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        Log.e("onCreate", mParam1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("BeautifulFragment", mParam1 + "onCreateView");

        View view = inflater.inflate(R.layout.fragment_beatiful_collection_base, container, false);
        ButterKnife.bind(this, view);
        initView();
        isCreateView = true;
        lazyLoad();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e("BeautifulFragment", mParam1 + "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            lazyLoad();
        }
    }

    public void initView(){
        mTvTest.setText(mParam1);
        initRv();
    }

    public void initRv(){
        imgListWithDescriptionList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new BeautifulRVAdapter(getActivity(), imgListWithDescriptionList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);

    }

    public void lazyLoad(){
        if(isCreateView && isVisible) {
            Log.e("BeautifulFragment", mParam1 + "lazyLoad");
            RetrofitThing.getInstance().onShowApiPicContentResponse(mParam1, new SubscriberByTag("init", this));
        }
    }

    @Override
    public void onCompleted(String tag) {
        Log.e("rx", "onCompleted");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String tag, Throwable e) {
        Log.e("BaseFragmentError", e.getMessage());
    }

    @Override
    public void onNext(String tag, Object o) {
        Log.e("rx", "onNext");
        if(o == null || ((PictureQueryResult)o).getShowapi_res_body() == null){
            Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
        }else{
            switch (tag){
                case "init":
                    imgListWithDescriptionList.clear();
                    PictureQueryResultBody resultBody = ((PictureQueryResult)o).getShowapi_res_body();
                    List<PictureContent> contentList = resultBody.getPagebean().getContentlist();
                    for(PictureContent content : contentList){
                        imgListWithDescriptionList.add(content);
                    }
                    break;
            }

        }
    }

    @Override
    public void onItemWithParamClick(View v, Object o) {
        List<PictureSizeUrl> sizeUrlList = (List<PictureSizeUrl>)o;
        Log.e("onItemWithParamClick", sizeUrlList.size() + "");
    }
}