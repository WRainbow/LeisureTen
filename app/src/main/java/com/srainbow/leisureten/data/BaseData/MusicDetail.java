package com.srainbow.leisureten.data.basedata;

import android.net.Uri;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SRainbow on 2016/10/12.
 */
public class MusicDetail implements Serializable{

    //歌曲名称
    public String title;
    //歌曲id
    public long id;
    //歌曲专辑
    public String artist;
    //歌曲时长
    public String duration;
    //歌曲大小
    public String size;
    //歌曲路径
    public String path;
    //歌曲专辑id
    public int albumId;
    //歌曲专辑信息
    public Uri albumUri;

    public MusicDetail(){

    }

    public void setId(long id) {
        this.id = id;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public void setBaseInfo(String title, String artist, long duration, long size, String path){
        this.title=title;
        this.artist=artist;
        this.duration=formatDuration(duration);
        this.size=formatSize(size);
        this.path=path;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setAlbumUri(Uri uri){
        this.albumUri=uri;
    }

    public String formatDuration(long duration){
        SimpleDateFormat mSDF = new SimpleDateFormat("m:ss");
        Date date = new Date(duration);
        return mSDF.format(date);
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getDuration() {
        return duration;
    }

    public String getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public int getAlbumId() {
        return albumId;
    }

    public Uri getAlbumUri() {
        return albumUri;
    }

    public String formatSize(long size){
        float floatSize=size/1024/1024f;
        return String.valueOf((double)((int)(floatSize*10+0.5))/10)+"M";
    }
}
