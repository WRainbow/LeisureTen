package com.srainbow.leisureten.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.HDPictureTagRVAdapter;
import com.srainbow.leisureten.adapter.SongMenuRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.custom.interfaces.OnTVWithUrlInRvClickToDoListener;
import com.srainbow.leisureten.custom.service.MusicPlayService;
import com.srainbow.leisureten.data.jsoupdata.TagDetail;
import com.srainbow.leisureten.data.realm.SongMenu;
import com.srainbow.leisureten.util.BaseUtil;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.DBOperation;
import com.srainbow.leisureten.util.HtmlParserWithJSoup;
import com.srainbow.leisureten.widget.RectangleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnTVWithUrlInRvClickToDoListener,
        NavigationView.OnNavigationItemSelectedListener, OnItemWithParamClickListener{

    @Bind(R.id.main_drawer)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.main_nav)
    NavigationView mNavigationView;
    @Bind(R.id.main_tb)
    Toolbar mToolbar;
    @Bind(R.id.main_fab)
    FloatingActionButton mFab;

    //drawer header view
    //can find by ButterKnife cause they are attribute of NavigationView
    private ImageView mIvPortrait;
    private TextView mTvUserName;

    @Bind(R.id.main_happy_cv_item1)
    RectangleImageView mHappyRectIvItem1;//默认为趣图
    @Bind(R.id.main_happy_cv_item2)
    RectangleImageView mHappyRectIvItem2;//默认为笑话
    @Bind(R.id.main_other_cv_item1)
    RectangleImageView mOtherRectIvItem1;//默认为生活
    @Bind(R.id.main_other_cv_item2)
    RectangleImageView mOtherRectIvItem2;//默认为娱乐
//    @Bind(R.id.main_other_cv_item3)
//    RectangleImageView mOtherRectIvItem3;//默认为写真
    @Bind(R.id.main_other_cv_item4)
    RectangleImageView mOtherRectIvItem4;//默认为新闻
    @Bind(R.id.main_other_cv_item5)
    RectangleImageView mOtherRectIvItem5;//默认为明星
    @Bind(R.id.main_other_cv_item6)
    RectangleImageView mOtherRectIvItem6;//默认为服装

    @Bind(R.id.main_classification_hdtag_rv)
    RecyclerView mRvHDTag;
    @Bind(R.id.main_classification_hdload_tv)
    TextView mTvLoading;

    private HDPictureTagRVAdapter mHDPictureRvAdapter;
    private List<TagDetail> tagDetailList;
    private SongMenuRVAdapter mSongMenuRVAdapter;
    private List<SongMenu> songMenuList;

    private String userName = "";

    private Realm mRealm;
    private AlertDialog showDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ButterKnife.bind(this);
            mRealm = DBOperation.getInstance().selectRealm(Constant.songMenuNameTable);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            initVar();
            initViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initViews(){
        initToolbar();
        initFab();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        mHDPictureRvAdapter = new HDPictureTagRVAdapter(this, tagDetailList);
        mHDPictureRvAdapter.setOnItemClickListener(this);
        mRvHDTag.setAdapter(mHDPictureRvAdapter);
        mRvHDTag.setLayoutManager(staggeredGridLayoutManager);

        mHappyRectIvItem1.setOnClickListener(this);
        mHappyRectIvItem2.setOnClickListener(this);
        mOtherRectIvItem1.setOnClickListener(this);
        mOtherRectIvItem2.setOnClickListener(this);
//        mOtherRectIvItem3.setOnClickListener(this);
        mOtherRectIvItem4.setOnClickListener(this);
        mOtherRectIvItem5.setOnClickListener(this);
        mOtherRectIvItem6.setOnClickListener(this);

        //find NavigationView's headerLayout
        View headerView = mNavigationView.getHeaderView(0);
        mIvPortrait = (ImageView)headerView.findViewById(R.id.nav_header_iv);
        mTvUserName = (TextView)headerView.findViewById(R.id.nav_header_username_tv);

        mNavigationView.setNavigationItemSelectedListener(this);
        mTvUserName.setOnClickListener(this);
        mIvPortrait.setOnClickListener(this);

    }

    public void initToolbar(){
    }

    //设置音乐播放控件状态
    public void setPlayMusicView() {
//        mFab.setBackgroundResource(R.drawable.ic_music_to_play);
        showMessageByString("初始");
        mFab.setImageResource(R.drawable.ic_music);
    }

    //设置音乐暂停控件状态
    public void setStopMusicView() {
//        mFab.setBackgroundResource(R.drawable.ic_music_to_stop);
        showMessageByString("");
    }

    public void initFab(){
        setPlayMusicView();
        mFab.setOnClickListener(this);
    }

    public void initVar(){
        tagDetailList = new ArrayList<>();
        songMenuList = new ArrayList<>();
        rxJava();

    }

    public void rxJava(){
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                Log.e("tag", "completed");
                mTvLoading.setVisibility(View.GONE);
                mHDPictureRvAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("tag", e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                if(o == null || ((List<TagDetail>)o).isEmpty()){
                    showMessageByString("没有数据");
                }else{
                    List<TagDetail> details = (List<TagDetail>)o;
                    tagDetailList.clear();
                    for(TagDetail detail : details){
                        tagDetailList.add(detail);
                    }
                }
            }
        };
        Observable.create(new Observable.OnSubscribe<List<TagDetail>>() {
            @Override
            public void call(Subscriber<? super List<TagDetail>> subscriber) {
                List<TagDetail> list = HtmlParserWithJSoup.getInstance().parserHtmlForTags(Constant.ADDRESS_PICJUMBO);
                if(list.get(0).getTag().equals("error")){
                    try {
                        subscriber.onError(new Exception(list.get(0).getUrl()));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                }

            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.main_happy_cv_item1:
                intent = new Intent(MainActivity.this, ContentShowActivity.class);
                intent.putExtra("classificationName", mHappyRectIvItem1.getImgText());
                startActivity(intent);
                break;
            case R.id.main_happy_cv_item2:
                intent = new Intent(MainActivity.this, ContentShowActivity.class);
                intent.putExtra("classificationName", mHappyRectIvItem2.getImgText());
                startActivity(intent);
                break;
            case R.id.main_other_cv_item1:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem1.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item2:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem2.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item4:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem4.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item5:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem5.getImgText()));
                startActivity(intent);
                break;
            case R.id.main_other_cv_item6:
                intent = new Intent(MainActivity.this, DetailClassificationActivity.class);
                intent.putExtra("classificationName", getCodeString(mOtherRectIvItem6.getImgText()));
                startActivity(intent);
                break;
            //portrait clicked
            case R.id.nav_header_iv:
                showMessageByString("Portrait");
                break;
            //userName clicked
            case R.id.nav_header_username_tv:
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), Constant.LOGIN_TAG);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
//
//            //Collection AlertDialog click listener
            case R.id.dialog_collection_joke_tv:
                intent = new Intent(MainActivity.this, ShowCollectionActivity.class);
                intent.putExtra("collectionType", "jokeCollection");
                startActivity(intent);
                showMessageByString("jokeCollection");
                break;
            case R.id.dialog_collection_picture_tv:
                intent = new Intent(MainActivity.this, ShowCollectionActivity.class);
                intent.putExtra("collectionType", "pictureCollection");
                startActivity(intent);
                showMessageByString("pictureCollection");
                break;
            case R.id.dialog_collection_atlas_tv:
                intent = new Intent(MainActivity.this, ShowCollectionActivity.class);
                intent.putExtra("collectionType", "atlasCollection");
                startActivity(intent);
                showMessageByString("atlasCollection");
                break;
            case R.id.main_fab:
                Log.e("mainActivity", "musicPlayType = " + musicPlay);
                switch (musicPlay) {
                    case Constant.PlayerMsg.PLAY_MSG:
                        playMusic();
                        musicPlay = Constant.PlayerMsg.PAUSE_MSG;
                        break;
                    case Constant.PlayerMsg.CONTINUE_MSG:
                        continueMusic();
                        musicPlay = Constant.PlayerMsg.PAUSE_MSG;
                        break;
                    case Constant.PlayerMsg.PAUSE_MSG:
                        pauseMusic();
                        musicPlay = Constant.PlayerMsg.CONTINUE_MSG;
                        break;
                }
                break;
            //创建新歌单
            case R.id.dialog_music_newlist_tv:
                final EditText mEtMenuName = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("新建歌单")
                        .setView(mEtMenuName)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                long songMenuId = BaseUtil.getInstance().getTimeStamp();
                                if (addMenuList(songMenuId, mEtMenuName.getText().toString())) {
                                    //转到添加歌曲页面
                                    Intent intent = new Intent(MainActivity.this, ShowMusicActivity.class);
                                    intent.putExtra("activityType", "create");
                                    intent.putExtra("songMenuId", songMenuId);
                                    intent.putExtra("songMenuName", mEtMenuName.getText().toString());
                                    startActivity(intent);
                                    showDialog.dismiss();
                                } else {
                                    showMessageByString("创建歌单失败");
                                }
                            }
                        })
                        .show();

                break;
            default:

        }
    }

    //主界面高清大图Tag点击事件
    @Override
    public void onTvItemClick(View v, Object o) {
        Intent intent = new Intent(MainActivity.this, HDPictureShowActivity.class);
        intent.putExtra("tag", ((TagDetail)o).getTag());
        intent.putExtra("tagUrl", ((TagDetail)o).getUrl());
        startActivity(intent);
    }

    //歌单点击事件
    @Override
    public void onItemWithParamClick(View v, Object... objects) {
        switch (v.getId()) {
            //歌单详情
            case R.id.song_menu_name_tv:
                Intent intent = new Intent(MainActivity.this, ShowMusicActivity.class);
                if ( 0 == (long)objects[0]) {
                    intent.putExtra("activityType", "default");
                } else {
                    intent.putExtra("activityType", "show");
                }
                if (musicPlay != Constant.PlayerMsg.PLAY_MSG &&
                        getSettingValueToSP(Constant.SP_SETTING_NAME, Constant.SETTING_SONG_MENU)
                        .equals(objects[2])) {
                    Intent musciIntent = new Intent(MainActivity.this, MusicPlayService.class);
                    stopService(musciIntent);
                    musicPlay = Constant.PlayerMsg.PLAY_MSG;
                    showMessageByString("修改歌单数据，音乐停止播放");
                }
                intent.putExtra("songMenuId", (long)objects[0]);
                startActivity(intent);
                break;
        }
    }

    //歌单长按事件
    @Override
    public void onItemWithParamLongClick(View v, final Object... objects) {
        switch (v.getId()) {
            //删除歌单
            case R.id.song_menu_name_tv:
                try {
                    new AlertDialog.Builder(this)
                            .setTitle("确定删除此歌单？")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final SongMenu menu = mRealm.where(SongMenu.class)
                                            .equalTo("menuId", (long)objects[0])
                                            .findFirst();
                                    if (null != menu) {
                                        mRealm.executeTransaction(new Realm.Transaction() {
                                            @Override
                                            public void execute(Realm realm) {
                                                menu.deleteFromRealm();
                                            }
                                        });
                                        songMenuList.remove((int)objects[1]);
                                        mSongMenuRVAdapter.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                                    }
                                    if (musicPlay != Constant.PlayerMsg.PLAY_MSG &&
                                            getSettingValueToSP(Constant.SP_SETTING_NAME, Constant.SETTING_SONG_MENU)
                                                    .equals(objects[2])) {
                                        Intent musciIntent = new Intent(MainActivity.this, MusicPlayService.class);
                                        stopService(musciIntent);
                                        musicPlay = Constant.PlayerMsg.PLAY_MSG;
                                        saveSettingValueFromSP(Constant.SP_SETTING_NAME,
                                                Constant.SETTING_SONG_MENU, Constant.DEFAULT_SONG_MENU);
                                        showMessageByString("播放歌单删除，已恢复默认");
                                    }
                                }
                            })
                            .show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    //侧滑栏菜单项点击事件
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_menu_about:
                showMessageByString("关于");
                break;
            case R.id.nav_menu_collection:
                setCollectionDialog();
                break;
            case R.id.nav_menu_feedback:
                showMessageByString("反馈");
                break;
            case R.id.nav_menu_music:
                setSongMenuDialog();
                break;
            case R.id.nav_menu_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
        return true;
    }

    //显示收藏弹窗
    public void setCollectionDialog(){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_collection,
                (ViewGroup)findViewById(R.id.dialog_collection));
        showDialog = new AlertDialog.Builder(this).setView(dialogView).show();
        TextView mTvJokeCollection = (TextView)dialogView.findViewById(R.id.dialog_collection_joke_tv);
        TextView mTvPictureCollection = (TextView)dialogView.findViewById(R.id.dialog_collection_picture_tv);
        TextView mTvAtlasCollection = (TextView)dialogView.findViewById(R.id.dialog_collection_atlas_tv);
        mTvJokeCollection.setOnClickListener(this);
        mTvPictureCollection.setOnClickListener(MainActivity.this);
        mTvAtlasCollection.setOnClickListener(MainActivity.this);

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
        mTvNewMenu.setOnClickListener(this);

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
            Toast.makeText(MainActivity.this, "歌单名称不能为空", Toast.LENGTH_SHORT).show();
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

    //播放音乐
    public void playMusic() {
        Intent musicIntent = new Intent(this, MusicPlayService.class);
        try {
            musicIntent.putExtra("MSG", Constant.PlayerMsg.PLAY_MSG);
            startService(musicIntent);       //启动服务
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        Intent intent=new Intent(this, MusicPlayService.class);
        intent.putExtra("MSG",Constant.PlayerMsg.PAUSE_MSG);
        startService(intent);       //启动服务
    }

    public void continueMusic() {
        Intent intent=new Intent(this, MusicPlayService.class);
        intent.putExtra("MSG",Constant.PlayerMsg.CONTINUE_MSG);
        startService(intent);       //启动服务
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Constant.LOGIN_TAG:
                if(resultCode == RESULT_OK){
                    if(data.getExtras().getString("userName") != null){
                        userName = data.getExtras().getString("userName");
                        mTvUserName.setText(userName);
                    }
                }
                break;
        }
    }

    public String getCodeString(String showString){
        switch (showString){
            case "生活":
                return "生活趣味";
            case "娱乐":
                return "娱乐八卦";
            case "写真":
                return "美女图片";
            case "新闻":
                return "社会百态";
            case "明星":
                return "明星写真";
            case "服装":
                return "时尚伊人";
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveUserNameToSP("null");

        if (mRealm != null) {
            mRealm.close();
        }

        Intent intent=new Intent(this, MusicPlayService.class);
        stopService(intent);       //停止服务

    }
}
