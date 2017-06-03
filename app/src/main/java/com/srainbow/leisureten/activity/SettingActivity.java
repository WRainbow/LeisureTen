package com.srainbow.leisureten.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.SongMenuRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.custom.service.MusicPlayService;
import com.srainbow.leisureten.data.realm.SongMenu;
import com.srainbow.leisureten.util.BaseUtil;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.DBOperation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class SettingActivity extends BaseActivity implements View.OnClickListener, OnItemWithParamClickListener{

    @Bind(R.id.setting_toolbar_include)
    Toolbar mToolbar;
    @Bind(R.id.layout_title_tv)
    TextView mTvTitle;
    @Bind(R.id.setting_menu_list_path_tv)
    TextView mTvSongMenuPath;
    @Bind(R.id.setting_menu_list_path_rlayout)
    RelativeLayout mRlayoutPath;

    private Realm mRealm;
    private AlertDialog showDialog;
    private SongMenuRVAdapter mSongMenuRVAdapter;
    private List<SongMenu> songMenuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mRealm = DBOperation.getInstance().selectRealm(Constant.songMenuNameTable);
        initVar();
        initView();
    }

    public void initVar() {
        songMenuList = new ArrayList<>();
    }

    public void initView() {
        initTb();
        String songMenuPath = getSettingValueToSP(Constant.SP_SETTING_NAME, Constant.SETTING_SONG_MENU);
        mTvSongMenuPath.setText(songMenuPath);
        mRlayoutPath.setOnClickListener(this);
    }

    public void initTb() {
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mTvTitle.setText("设置");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_menu_list_path_rlayout:
                setSongMenuDialog();
                break;
        }
    }

    @Override
    public void onItemWithParamClick(View v, Object... objects) {
        switch (v.getId()) {
            //歌单详情
            case R.id.song_menu_name_tv:
                showDialog.dismiss();
                if (!getSettingValueToSP(Constant.SP_SETTING_NAME,
                        Constant.SETTING_SONG_MENU).equals((String)objects[2])) {
                    mTvSongMenuPath.setText((String)objects[2]);
                    saveSettingValueFromSP(Constant.SP_SETTING_NAME,
                            Constant.SETTING_SONG_MENU, (String)objects[2]);
                    Intent intent = new Intent(SettingActivity.this, MusicPlayService.class);
                    stopService(intent);
                    musicPlay = Constant.PlayerMsg.PLAY_MSG;
                    showMessageByString("歌单已更改，请重新播放");
                }
                break;
        }
    }

    @Override
    public void onItemWithParamLongClick(View v, final Object... objects) {

    }


    //显示歌单弹窗
    public void setSongMenuDialog() {
        View view = setSongMenuView();
        showDialog = new AlertDialog.Builder(this).setView(view).show();
    }

    //设置歌单显示界面
    public View setSongMenuView() {
        //找出布局及控件
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_music,
                (ViewGroup)findViewById(R.id.dialog_music));
        RecyclerView mRvMenu = (RecyclerView)view.findViewById(R.id.dialog_music_rv);
        TextView mTvNewMenu = (TextView)view.findViewById(R.id.dialog_music_newlist_tv);
        mTvNewMenu.setVisibility(View.GONE);

        getSongMenuList();
        //初始化RecyclerView
        mSongMenuRVAdapter = new SongMenuRVAdapter(this, songMenuList);
        mSongMenuRVAdapter.setItemOnClickListener(this);
        mRvMenu.setAdapter(mSongMenuRVAdapter);
        mRvMenu.setLayoutManager(new LinearLayoutManager(this));
        return view;
    }

    //获取歌单列表
    public void getSongMenuList() {

        songMenuList.clear();
        //添加默认手机歌曲歌单
        SongMenu defaultMusicList = new SongMenu();
        defaultMusicList.setMenuName(Constant.DEFAULT_SONG_MENU);
        defaultMusicList.setMenuId(0);
        songMenuList.add(defaultMusicList);
        //从数据库中查找音乐歌单数据
        RealmResults<SongMenu> list = mRealm.where(SongMenu.class)
                .findAll();
        for ( SongMenu songMenu : list) {
            songMenuList.add(songMenu);
        }
    }

    //添加新歌单
    public boolean addMenuList(final long id, final String name){
        if (name.isEmpty()) {
            Toast.makeText(this, "歌单名称不能为空", Toast.LENGTH_SHORT).show();
        } else {
            //查询数据库中是否存在歌单名
            RealmResults<SongMenu> list = mRealm.where(SongMenu.class)
                    .equalTo("menuName", name)
                    .findAll();
            if (list.size() > 0) {
                //歌单名已存在
                Toast.makeText(this, "歌单已存在", Toast.LENGTH_SHORT).show();
            } else {
                //写入数据
                mRealm.executeTransaction(new Realm.Transaction() {
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
