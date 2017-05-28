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
    private static RequestApi juheApi;
    private static RequestApi juheRandomApi;
    private static RequestApi showApi;
    private static RequestApi backgroundApi;

    private RequestApi getBackgroundApi(){
        if(backgroundApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.PHONE_IP)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            backgroundApi = retrofit.create(RequestApi.class);
        }
        return backgroundApi;
    }

    private RequestApi getJuHeApi(){
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

    private RequestApi getJuheRandomApi () {
        if(juheRandomApi == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASERURL_JUHU_RANDOM)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            juheRandomApi = retrofit.create(RequestApi.class);
        }
        return juheRandomApi;
    }

    private RequestApi getShowApi(){
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

    public void onFunnyPicResponse(String sort, int page, int size, String time, SubscriberByTag subscriber){
        getJuHeApi().getFunnyPicData(sort, page, size, time, Constant.JUHE_KEY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onRandomFunnyPicResponse (SubscriberByTag subscriber) {
        getJuheRandomApi().getRandomFunnyPicData(Constant.JUHE_KEY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onJokeResponse(String sort, int page, int size, String time, SubscriberByTag subscriber){
        getJuHeApi().getJokeData(sort, page, size, time, Constant.JUHE_KEY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void onRandomJokeResponse(SubscriberByTag subscriber){
        getJuheRandomApi().getRandomJokeData(Constant.JUHE_KEY)
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

    public void onLoginResponse(String username, String password, SubscriberByTag subscriber){
        getBackgroundApi().getLoginResult(username, password)
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
