package com.srainbow.leisureten.netRequest;


import com.srainbow.leisureten.netRequest.reWriteWay.SubscriberByTag;
import com.srainbow.leisureten.util.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SRainbow on 2016/9/29.
 */
public class RetrofitThing {
    public static RequestApi juheApi;
    public static RequestApi showApi;

    public RequestApi getJuHeApi(){
        if(juheApi == null){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(Constant.BASERURL_JUHU)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            juheApi =retrofit.create(RequestApi.class);
        }
        return juheApi;
    }

    public RequestApi getShowApi(){
        if(showApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASEURL_PICCLASSIFICATIONURL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            showApi = retrofit.create(RequestApi.class);
        }
        return showApi;
    }

    public void onFunnyPicResponse(SubscriberByTag subscriber){
        getJuHeApi().getFunnyPicData(Constant.JUHE_KEY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onJokeResponse(SubscriberByTag subscriber){
        getJuHeApi().getJokeData(Constant.JUHE_KEY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onShowApiPicClassificationResponse(SubscriberByTag subscriber){
        getShowApi().getShowApiPicData(Constant.SHOWAPI_APPID, Constant.SHOWAPI_SIGN)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onShowApiPicContentResponse(String type, String page, SubscriberByTag subscriber){
        getShowApi().getShowApiContentData(Constant.SHOWAPI_APPID, Constant.SHOWAPI_SIGN,
                type, page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final RetrofitThing INSTANCE = new RetrofitThing();
    }

    //获取单例
    public static RetrofitThing getInstance(){
        return SingletonHolder.INSTANCE;
    }

}
