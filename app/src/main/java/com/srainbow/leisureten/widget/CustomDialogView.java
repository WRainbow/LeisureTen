package com.srainbow.leisureten.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.activity.MainActivity;
import com.srainbow.leisureten.adapter.SongMenuRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.data.realm.SongMenu;
import com.srainbow.leisureten.util.BaseUtil;
import com.srainbow.leisureten.util.Constant;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by SRainbow on 2017/6/3.
 */

public class CustomDialogView {

    private static volatile CustomDialogView instance = null;

    public static CustomDialogView getInstance() {
        if (instance == null) {
            synchronized (CustomDialogView.class) {
                if (instance == null) {
                    instance = new CustomDialogView();
                }
            }
        }
        return instance;
    }
    //显示歌单弹窗
    /*

     */
    public void setSongMenuDialog(Context context, View parent, View.OnClickListener onClickListener,
                                  OnItemWithParamClickListener onItemWithParamClickListener,
                                  Realm realm) {
        View view = setSongMenuView(context, parent, onClickListener, onItemWithParamClickListener, realm);
        new AlertDialog.Builder(context).setView(view).show();
    }

    //设置歌单显示界面
    private View setSongMenuView(Context context, View parent, View.OnClickListener onClickListener,
                                OnItemWithParamClickListener onItemWithParamClickListener,
                                 Realm realm) {
        //找出布局及控件
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_music,
                (ViewGroup)parent.findViewById(R.id.dialog_music));
        RecyclerView mRvMenu = (RecyclerView)view.findViewById(R.id.dialog_music_rv);
        TextView mTvNewMenu = (TextView)view.findViewById(R.id.dialog_music_newlist_tv);
        mTvNewMenu.setOnClickListener(onClickListener);


        List<SongMenu> songMenuList = getSongMenuList(realm);
        //初始化RecyclerView
        SongMenuRVAdapter mSongMenuRVAdapter = new SongMenuRVAdapter(context, songMenuList);
        mSongMenuRVAdapter.setItemOnClickListener(onItemWithParamClickListener);
        mRvMenu.setAdapter(mSongMenuRVAdapter);
        mRvMenu.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    //获取歌单列表
    private List<SongMenu> getSongMenuList(Realm realm) {

        List<SongMenu> songMenuList = new ArrayList<>();
        //添加默认手机歌曲歌单
        SongMenu defaultMusicList = new SongMenu();
        defaultMusicList.setMenuName(Constant.DEFAULT_SONG_MENU);
        defaultMusicList.setMenuId(0);
        songMenuList.add(defaultMusicList);
        //从数据库中查找音乐歌单数据
        RealmResults<SongMenu> list = realm.where(SongMenu.class)
                .findAll();
        for ( SongMenu songMenu : list) {
            songMenuList.add(songMenu);
        }
        return songMenuList;
    }

}
