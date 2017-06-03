package com.srainbow.leisureten.util;

import android.content.Context;
import android.widget.Toast;

import com.srainbow.leisureten.data.realm.SongMenu;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by SRainbow on 2017/6/1.
 */

public class DBOperation {
    private static volatile DBOperation instance = null;

    public static DBOperation getInstance() {
        if (instance == null) {
            synchronized (DBOperation.class) {
                if (instance == null) {
                    instance = new DBOperation();
                }
            }
        }
        return instance;
    }

    public Realm selectRealm(String name) {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(name)
                .build();
        return Realm.getInstance(configuration);
    }

    //添加新歌单
    public boolean addMenuList(Context context, Realm realm, final long id, final String name){
        if (name.isEmpty()) {
            Toast.makeText(context, "歌单名称不能为空", Toast.LENGTH_SHORT).show();
        } else {
            //查询数据库中是否存在歌单名
            RealmResults<SongMenu> list = realm.where(SongMenu.class)
                    .equalTo("menuName", name)
                    .findAll();
            if (list.size() > 0) {
                //歌单名已存在
                Toast.makeText(context, "歌单已存在", Toast.LENGTH_SHORT).show();
            } else {
                //写入数据
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        SongMenu menu = new SongMenu();
                        menu.setMenuId(id);
                        menu.setMenuName(name);
                        realm.copyToRealmOrUpdate(menu);
                    }
                });
                return true;
            }
        }
        return false;
    }

}
