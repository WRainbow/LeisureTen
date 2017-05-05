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
    public volatile static RequestApi requestApi;

    public RequestApi getRequestApi(String baseUrl){
        if(requestApi == null){
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            requestApi=retrofit.create(RequestApi.class);
        }
        return requestApi;
    }

    public void onFunnyPicResponse(SubscriberByTag subscriber){
        getRequestApi(Constant.BASERURL_JUHU).getFunnyPicData("b3c10341bc734b752fa7cb47b1fb0641")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onJokeResponse(SubscriberByTag subscriber){
        getRequestApi(Constant.BASERURL_JUHU).getJokeData("b3c10341bc734b752fa7cb47b1fb0641")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onShowApiPicClassificationResponse(SubscriberByTag subscriber){
        getRequestApi(Constant.BASEURL_PICCLASSIFICATIONURL).getShowApiPicData(Constant.SHOWAPI_APPID, Constant.SHOWAPI_SIGN)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onShowApiPicContentResponse(String type, SubscriberByTag subscriber){
        getRequestApi(Constant.BASEURL_PICCLASSIFICATIONURL).getShowApiContentData(Constant.SHOWAPI_APPID, Constant.SHOWAPI_SIGN,
                type)
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
