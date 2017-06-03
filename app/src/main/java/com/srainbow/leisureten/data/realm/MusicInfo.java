package com.srainbow.leisureten.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by SRainbow on 2017/6/1.
 */

public class MusicInfo extends RealmObject {

    //主键
    @PrimaryKey
    private String unique;

    //歌曲名称
    private String title;
    //演唱者
    private String artist;
    //歌曲时长
    private long duration;
    //歌曲大小
    private long size;
    //歌曲路径
    private String path;
    //歌曲专辑id
    private int albumId;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {

        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String getUnique() {
        return unique;
    }
}
