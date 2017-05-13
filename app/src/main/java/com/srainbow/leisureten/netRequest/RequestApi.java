package com.srainbow.leisureten.netRequest;


import com.srainbow.leisureten.data.APIData.FunnyPicData;
import com.srainbow.leisureten.data.APIData.JokeData;
import com.srainbow.leisureten.data.APIData.showapi.picture_classification.PictureClassificationResult;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureQueryResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by SRainbow on 2016/9/29.
 */
public interface RequestApi {
    @GET("joke/randJoke.php?type=pic")
    Observable<FunnyPicData> getFunnyPicData(@Query("key") String key);
    @GET("joke/randJoke.php?type=")
    Observable<JokeData> getJokeData(@Query("key")String key);
    @GET("852-1")
    Observable<PictureClassificationResult> getShowApiPicData(@Query("showapi_appid") String app_id,
                                                              @Query("showapi_sign") String sign);
    @GET("852-2")
    Observable<PictureQueryResult> getShowApiContentData(@Query("showapi_appid") String app_id,
                                                         @Query("showapi_sign") String sign,
                                                         @Query("type") String type,
                                                         @Query("page") String page);

}
