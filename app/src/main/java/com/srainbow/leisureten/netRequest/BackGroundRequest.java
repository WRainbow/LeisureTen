package com.srainbow.leisureten.netRequest;

import com.srainbow.leisureten.data.APIData.FunnyPicDetail;
import com.srainbow.leisureten.data.APIData.ImgWithAuthor;
import com.srainbow.leisureten.data.APIData.JokeDetail;
import com.srainbow.leisureten.data.APIData.showapi.picture_query.PictureContent;

import java.util.ArrayList;

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

    //confirm username and password is match or not
    public boolean checkUser(String name, String psw){
        return true;
    }

    //add beautiful picture atlas collection into database
    public boolean addBeautifulAtlas(PictureContent pictureContent){
        return true;
    }

    //add beautiful picture collection into database
    public boolean addBeautifulPicture(String imgUrl, ArrayList<String> collectionInfo){
        return true;
    }

    //add HD picture collection into database
    public boolean addHDPicture(ImgWithAuthor imgWithAuthor){
        return true;
    }

    //add funny picture collection into database
    public boolean addFunnyPic(FunnyPicDetail detail){
        return true;
    }

    //add joke collection into database
    public boolean addJoke(JokeDetail detail){
        return true;
    }

    //delete beautiful picture atlas collection from database
    public boolean deleteBeautifulAtlas(PictureContent pictureContent){
        return true;
    }

    //delete beautiful picture collection from database
    public boolean deleteBeautifulPicture(String imgUrl, ArrayList<String> collectionInfo){
        return true;
    }

    //delete HD picture collection from database
    public boolean deletedHDPicture(ImgWithAuthor imgWithAutho){
        return true;
    }

    //delete funny picture collection from database
    public boolean deleteFunnyPic(FunnyPicDetail detail){
        return true;
    }

    //delete joke collection from database
    public boolean deleteJoke(JokeDetail detail){
        return true;
    }

    public boolean isBeautifulAtlasExist(PictureContent pictureContent){
        return false;
    }

    public boolean isBeautifulPictureExist(String imgUrl){
        return false;
    }

    public boolean isHDPictureExist(String imgUlr){
        return false;
    }

    public boolean isFunnyPicExist(FunnyPicDetail detail){
        return false;
    }

    public boolean isJokeExist(JokeDetail detail){
        return false;
    }

    public boolean downLoadImage(String url){
        return false;
    }

    public boolean downLoadAtlas(PictureContent pictureContent){
        return false;
    }

}
