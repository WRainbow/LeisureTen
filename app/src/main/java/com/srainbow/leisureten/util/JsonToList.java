package com.srainbow.leisureten.util;

import com.srainbow.leisureten.data.apidata.juhe.joke.JokeDetail;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureContent;
import com.srainbow.leisureten.data.apidata.showapi.picture_query.PictureSizeUrl;
import com.srainbow.leisureten.data.jsoupdata.ImgWithAuthor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SRainbow on 2017/5/30.
 */

public class JsonToList {
    private static volatile JsonToList instance = null;

    public static JsonToList getInstance() {
        if (instance == null) {
            synchronized (JsonToList.class) {
                if (instance == null) {
                    instance = new JsonToList();
                }
            }
        }
        return instance;
    }

    public List<ImgWithAuthor> getAllPictureCollection(JSONObject result){
        List<ImgWithAuthor> imgWithAuthorList = new ArrayList<>();
        JSONArray resultArray = result.optJSONArray("result");
        JSONObject ob;
        ImgWithAuthor imgWithAuthor = new ImgWithAuthor();
        for (int i = 0; i < resultArray.length(); i++) {
            ob = resultArray.optJSONObject(i);
            if ("funnyPicture".equals(ob.optString("pictureType"))) {
                imgWithAuthor.setImgUrl(ob.optString("url"));
                imgWithAuthor.setImgAuthor(ob.optString("content"));
                imgWithAuthorList.add(imgWithAuthor);
            }
            if ("beautifulPicture".equals(ob.optString("pictureType"))) {
                imgWithAuthor.setImgUrl(ob.optString("url"));
                imgWithAuthor.setImgAuthor(ob.optString("title"));
                imgWithAuthorList.add(imgWithAuthor);
            }
            if ("hdPicture".equals(ob.optString("pictureType"))) {
                imgWithAuthor.setImgUrl(ob.optString("url"));
                imgWithAuthor.setImgAuthor(ob.optString("author"));
                imgWithAuthorList.add(imgWithAuthor);
            }
        }
        return imgWithAuthorList;
    }

    public List<JokeDetail> getJokeCollection(JSONObject result) {
        List<JokeDetail> jokeDetailList = new ArrayList<>();
        JSONArray resultArray = result.optJSONArray("result");
        JSONObject ob;
        JokeDetail jokeDetail = new JokeDetail();
        for (int i = 0; i < resultArray.length(); i ++) {
            ob = resultArray.optJSONObject(i);
            jokeDetail.setHashId(ob.optString("hashId"));
            jokeDetail.setContent(ob.optString("content"));
            jokeDetailList.add(jokeDetail);
        }
        return jokeDetailList;
    }

    public List<PictureContent> getAtlasCollection(JSONObject result) {
        List<PictureContent> pictureContentList = new ArrayList<>();
        JSONArray resultArray = result.optJSONArray("result");
        JSONObject ob;
        JSONArray ar;
        JSONObject ob1;
        PictureContent pictureContent = new PictureContent();
        PictureSizeUrl pictureSizeUrl = new PictureSizeUrl();
        for (int i = 0; i < resultArray.length(); i ++) {
            ob = resultArray.optJSONObject(i);
            ar = ob.optJSONArray("pictureArray");
            List<PictureSizeUrl> pictureSizeUrlList = new ArrayList<>();
            for(int j = 0; j < ar.length(); j ++) {
                ob1 = ar.optJSONObject(j);
                pictureSizeUrl.setBig(ob1.optString("big"));
                pictureSizeUrl.setMiddle(ob1.optString("middle"));
                pictureSizeUrl.setSmall(ob1.optString("small"));
                pictureSizeUrlList.add(pictureSizeUrl);
            }
            pictureContent.setList(pictureSizeUrlList);
            pictureContent.setItemId(ob.optString("itemId"));
            pictureContent.setCt(ob.optString("ct"));
            pictureContent.setTitle(ob.optString("title"));
            pictureContent.setTypeName(ob.optString("typeName"));
            pictureContent.setType(ob.optString("type"));
            pictureContentList.add(pictureContent);
        }
        return pictureContentList;
    }
}
