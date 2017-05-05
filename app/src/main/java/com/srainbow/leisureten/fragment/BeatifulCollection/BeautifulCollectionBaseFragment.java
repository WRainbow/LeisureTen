package com.srainbow.leisureten.fragment.BeatifulCollection;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.BeautifulRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnShowMoreIVInRvClickToDoListener;
import com.srainbow.leisureten.data.APIData.ImgListWithDescription;
import com.srainbow.leisureten.data.APIData.ShowApiPicContentData;
import com.srainbow.leisureten.data.APIData.ShowApiPicContentDetail;
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
        OnShowMoreIVInRvClickToDoListener{

    @Bind(R.id.beautiful_base_rv)
    RecyclerView mRvShowPic;

    private LinearLayoutManager mLinearLayoutManager;
    private BeautifulRVAdapter mBeautifulRVAdapter;
    private List<ImgListWithDescription> imgListWithDescriptionList;

    public BeautifulCollectionBaseFragment() {
        
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BeautifulCollectionBaseFragment.
     */
    public static BeautifulCollectionBaseFragment newInstance() {
        BeautifulCollectionBaseFragment fragment = new BeautifulCollectionBaseFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_beatiful_collection_base, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void initView(){

    }

    public void initRv(){
        initVar();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mBeautifulRVAdapter = new BeautifulRVAdapter(getActivity(), imgListWithDescriptionList);
        mBeautifulRVAdapter.setOnItemClickListener(this);
        mRvShowPic.setAdapter(mBeautifulRVAdapter);
        mRvShowPic.setLayoutManager(mLinearLayoutManager);
    }

    public void initVar(){
        imgListWithDescriptionList = new ArrayList<>();
        RetrofitThing.getInstance().onShowApiPicContentResponse("4003", new SubscriberByTag("init", this));
    }

    @Override
    public void onCompleted(String tag) {

    }

    @Override
    public void onError(String tag, Throwable e) {

    }

    @Override
    public void onNext(String tag, Object o) {
        if(o == null){
            Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
        }else{
            switch (tag){
                case "init":
                    imgListWithDescriptionList.clear();
                    ShowApiPicContentDetail contentDetailList = ((ShowApiPicContentData)o).getShowapi_res_body();
                    List<ShowApiPicContentDetail.PicContent> contentList = contentDetailList.getPagebean().getContentlist();
                    if(contentList != null && contentList.size() > 0){
                        for(ShowApiPicContentDetail.PicContent content : contentList){
                            imgListWithDescriptionList.add(new ImgListWithDescription(content.getList(), content.getTitle()));
                        }
                    }
                    mBeautifulRVAdapter.notifyDataSetChanged();
                    break;
            }

        }


    }

    @Override
    public void onIVItemClick(View v, List<String> imgUrlList) {
        Log.e("size: ", imgUrlList.size() + "");
    }
}