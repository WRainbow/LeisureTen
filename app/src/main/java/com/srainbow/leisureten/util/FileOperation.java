package com.srainbow.leisureten.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import com.srainbow.leisureten.data.realm.MusicInfo;
import com.srainbow.leisureten.data.realm.SongMenu;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by SRainbow on 2017/6/1.
 */

public class FileOperation {
    private static volatile FileOperation instance = null;

    public static FileOperation getInstance() {
        if (instance == null) {
            synchronized (FileOperation.class) {
                if (instance == null) {
                    instance = new FileOperation();
                }
            }
        }
        return instance;
    }

    public List<MusicInfo> getLocalMusic(Context context){
        List<MusicInfo> musicDetails=new ArrayList<>();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if(cursor!=null) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    MusicInfo musicDetail = new MusicInfo();
                    cursor.moveToNext();
                    String title = cursor.getString((cursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE)));            //音乐标题
                    String artist = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));            //艺术家
                    long duration = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DURATION));          //时长
                    long size = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media.SIZE));              //文件大小
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));              //文件路径
                    int albumId=cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                    int isMusic = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));          //是否为音乐
                    if (isMusic != 0 && duration >= 1000 * 60*3) {     //只把时长大于2min的音乐添加到集合当中
                        musicDetail.setTitle(title);
                        musicDetail.setArtist(artist);
                        musicDetail.setDuration(duration);
                        musicDetail.setSize(size);
                        musicDetail.setPath(path);
                        musicDetail.setAlbumId(albumId);
                        musicDetail.setUnique(title + "-" + artist);
                        musicDetails.add(musicDetail);
                    }
                }
                Log.e("music", cursor.getCount() + "");
                Log.e("music", "isMusic有" + musicDetails.size());
                cursor.close();
            }
        }
        return musicDetails;
    }

    public List<MusicInfo> getSongMenuMusic(Context context, String name) {
        long musicMenuId = DBOperation.getInstance().selectRealm(Constant.songMenuNameTable)
                .where(SongMenu.class)
                .equalTo("menuName", name)
                .findFirst().getMenuId();
        return DBOperation.getInstance().selectRealm(String.valueOf(musicMenuId))
                .where(MusicInfo.class)
                .findAll();
    }

    public void saveUserNameToSP(Context context, String userName) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_USERNAME_NAME, MODE_PRIVATE);
        sp.edit().putString("userName", userName).apply();
    }

    public String getUserNameFromSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SP_USERNAME_NAME, MODE_PRIVATE);
        return sp.getString("userName", "null");
    }

    public void saveSettingValueFromSP(Context context, String name, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(name, MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public String getSettingValueToSP(Context context, String name, String key) {
        SharedPreferences sp = context.getSharedPreferences(name, MODE_PRIVATE);
        return sp.getString(key, "null");
    }

}
