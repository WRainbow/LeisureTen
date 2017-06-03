package com.srainbow.leisureten.application;

import android.app.Application;
import android.content.SharedPreferences;

import com.srainbow.leisureten.util.Constant;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by SRainbow on 2017/6/1.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化Realm数据库
        Realm.init(this);

        //初始化设置信息
        //初始化默认歌单信息
        SharedPreferences sp = getSharedPreferences(Constant.SP_SETTING_NAME, MODE_PRIVATE);
        if ("null".equals(sp.getString(Constant.SETTING_SONG_MENU, "null"))) {
            sp.edit().putString(Constant.SETTING_SONG_MENU, Constant.DEFAULT_SONG_MENU).apply();
        }
    }

}
