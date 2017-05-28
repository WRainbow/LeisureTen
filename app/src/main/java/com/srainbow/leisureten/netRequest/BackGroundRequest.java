package com.srainbow.leisureten.netRequest;

import android.util.Log;

import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.data.apidata.juhe.funnypicture.FunnyPicDetail;
import com.srainbow.leisureten.data.jsoupdata.ImgWithAuthor;
import com.srainbow.leisureten.data.apidata.juhe.joke.JokeDetail;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureContent;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureSizeUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by SRainbow on 2017/5/14.
 */

public class BackGroundRequest {
    private volatile static BackGroundRequest instance = null;

    private BackGroundRequest(){}

    public static BackGroundRequest getInstance(){
        if(instance == null){
            synchronized(BackGroundRequest.class){
                if(instance == null){
                    instance = new BackGroundRequest();
                }
            }
        }
        return instance;
    }

    public void loginConfirm(OnResponseListener listener, int tag, String name, String psw){
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "login");
        task.setParam("userName", name);
        task.setParam("passWord", psw);
        task.execute();
    }

    public void registerConfirm(OnResponseListener listener, int tag, String name, String psw){
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "register");
        task.setParam("userName", name);
        task.setParam("passWord", psw);
        task.execute();
    }

    //confirm username and password is match or not
    public boolean checkUser(String name, String psw){
        return true;
    }

    //add beautiful picture atlas collection into database
    public void addBeautifulAtlas(OnResponseListener listener, int tag, String userName, PictureContent pictureContent){
        JSONObject atlasObject = new JSONObject();
        JSONArray pictureArray = new JSONArray();
        try {
            for(PictureSizeUrl pictureSizeUrl : pictureContent.getList()){
                JSONObject pictureObject = new JSONObject();
                pictureObject.put("big", pictureSizeUrl.getBig());
                pictureObject.put("middle", pictureSizeUrl.getMiddle());
                pictureObject.put("small", pictureSizeUrl.getSmall());
                pictureArray.put(pictureObject);
            }
            atlasObject.put("pictureArray", pictureArray);
            atlasObject.put("itemId", pictureContent.getItemId());
            atlasObject.put("typeName", pictureContent.getTypeName());
            atlasObject.put("ct", pictureContent.getCt());
            atlasObject.put("title", pictureContent.getTitle());
            atlasObject.put("itemId", pictureContent.getItemId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "addAtlas");
        task.setParam("userName", userName);
        task.setParam("atlasObject", atlasObject.toString());
        task.execute();
    }

    //add beautiful picture collection into database
    public void addBeautifulPicture(OnResponseListener listener, int tag,
                                    String userName, String imgUrl, ArrayList<String> collectionInfo){
       JSONObject pictureObject = new JSONObject();
        try {
            pictureObject.put("hashId", getHashIdByUUID());
            pictureObject.put("url", imgUrl);
            //collectionInfo中参数顺序参照BeautifulCollectionBaseFragment中214 - 221行代码
            pictureObject.put("typeName", collectionInfo.get(0));
            pictureObject.put("title", collectionInfo.get(1));
            pictureObject.put("type", collectionInfo.get(2));
            pictureObject.put("ct", collectionInfo.get(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "addBeautifulPicture");
        task.setParam("userName", userName);
        task.setParam("pictureObject", pictureObject.toString());
        task.execute();
    }

    //add HD picture collection into database
    public void addHDPicture(OnResponseListener listener, int tag, String userName, ImgWithAuthor imgWithAuthor){
        JSONObject pictureObject = new JSONObject();
        try {
            pictureObject.put("hashId", getHashIdByUUID());
            pictureObject.put("url", imgWithAuthor.getImgUrl());
            pictureObject.put("author", imgWithAuthor.getImgAuthor());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "addHDPicture");
        task.setParam("userName", userName);
        task.setParam("pictureObject", pictureObject.toString());
        task.execute();

    }

    //add funny picture collection into database
    public void addFunnyPic(OnResponseListener listener, int tag, String userName, FunnyPicDetail detail){
        JSONObject pictureObject = new JSONObject();
        try {
            pictureObject.put("hashId", detail.getHashId());
            pictureObject.put("content", detail.getContent());
            pictureObject.put("unixTime", detail.getUnixtime());
            pictureObject.put("updateTime", detail.getUpdatetime());
            pictureObject.put("url", detail.getUrl());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "addFunnyPic");
        task.setParam("userName", userName);
        task.setParam("pictureObject", pictureObject.toString());
        task.execute();

    }

    //add joke collection into database
    public void addJoke(OnResponseListener listener, int tag, String userName, JokeDetail detail){
        JSONObject jokeObject = new JSONObject();
        try {
            Log.e("request", detail.getUnixtime());
            jokeObject.put("hashId", detail.getHashId());
            jokeObject.put("content", detail.getContent());
            jokeObject.put("unixTime", detail.getUnixtime());
            jokeObject.put("updateTime", detail.getUpdatetime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "addJoke");
        task.setParam("userName", userName);
        task.setParam("jokeObject", jokeObject.toString());
        task.execute();
    }

    //delete beautiful picture atlas collection from database
    public void deleteBeautifulAtlas(OnResponseListener listener, int tag, String userName, String deleteKey){
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "deleteAtlas");
        task.setParam("userName", userName);
        task.setParam("deleteKey", deleteKey);
        task.execute();
    }

    public void deletePicture (OnResponseListener listener, int tag, String userName, String deleteKey) {
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "deletePicture");
        task.setParam("userName", userName);
        task.setParam("deleteKey", deleteKey);
        task.execute();
    }

    //delete joke collection from database
    public void deleteJoke(OnResponseListener listener, int tag, String userName, String deleteKey){
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "deletePicture");
        task.setParam("userName", userName);
        task.setParam("deleteKey", deleteKey);
        task.execute();
    }

    public void isPictureExist(OnResponseListener listener, int tag, String userName, String imgUrl){
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "checkPicture");
        task.setParam("userName", userName);
        task.setParam("checkKey", imgUrl);
        task.execute();
    }

    public void isJokeExist(OnResponseListener listener, int tag, String userName, JokeDetail detail){
        BackgroundRequestTask task = new BackgroundRequestTask(listener, tag);
        task.setParam("method", "checkJoke");
        task.setParam("userName", userName);
        task.setParam("checkKey", detail.getHashId());
        task.execute();
    }

    public boolean downLoadImage(String url){
        return false;
    }

    public boolean downLoadAtlas(PictureContent pictureContent){
        return false;
    }

    public String getHashIdByUUID(){
        String str = UUID.randomUUID().toString();
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18)
                + str.substring(19, 23) + str.substring(24);
    }
}
