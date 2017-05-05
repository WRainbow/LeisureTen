package com.srainbow.leisureten.util;

import com.srainbow.leisureten.data.APIData.ImgWithAuthor;
import com.srainbow.leisureten.data.APIData.TagData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by SRainbow on 2017/4/10.
 */

public class ListToJson {
    private volatile static ListToJson instance = null;

    private ListToJson(){}

    public static ListToJson getInstance(){
        if(instance == null){
            synchronized (ListToJson.class){
                if(instance == null){
                    instance = new ListToJson();
                }
            }
        }
        return instance;
    }

    public JSONObject imgWithAuthorListToJson(List<ImgWithAuthor> list){
        JSONObject jsonObject = new JSONObject();
        if(list != null && list.size() > 0){
            for(ImgWithAuthor item : list){
                boolean isTransRight = false;
                try {
                    String str = "{"
                            + "\"imgUrl\":\""+item.getImgUrl()+"\""
                            + ","
                            + "\"imgAuthor\":\""+item.getImgAuthor()+"\""
                            + "}";
                    System.out.println(str);
                    jsonObject.getJSONObject(str);
                    //转换成功
                    jsonObject.put("code", 0);
                    isTransRight = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!isTransRight){
                    try {
                        jsonObject.put("code", 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jsonObject;
    }

    public JSONObject tagWithUrlListToJson(List<TagData> list){
        JSONObject jsonObject = new JSONObject();
        if(list != null && list.size() > 0){
            for(TagData item : list){
                if(item.getCode() == 0){
                    try {
                        String str = "{" +
                                    "\"code\":\"" + item.getCode() + "\""+
                                    "\"data\":{"+
                                        "\"tag\":\"" + item.getData().getTag() + "\""+
                                         ","+
                                         "\"url\":\"" + item.getData().getUrl() + "\""+
                                         "}" +
                                "}";
                        jsonObject.getJSONObject(str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jsonObject;
    }

}
