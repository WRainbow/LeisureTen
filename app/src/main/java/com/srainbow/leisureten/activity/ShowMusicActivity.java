package com.srainbow.leisureten.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.adapter.MusicRVAdapter;
import com.srainbow.leisureten.custom.interfaces.OnItemWithParamClickListener;
import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.data.realm.MusicInfo;
import com.srainbow.leisureten.netRequest.MusicAsyncTask;
import com.srainbow.leisureten.util.BaseUtil;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.DBOperation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class ShowMusicActivity extends BaseActivity implements OnItemWithParamClickListener,
        OnResponseListener {

    @Bind(R.id.show_music_rv)
    RecyclerView mRvShowMusic;
    @Bind(R.id.show_music_tip_tv)
    TextView mTvTips;
    @Bind(R.id.show_music_add_rlayout)
    RelativeLayout mRlayoutAdd;
    @Bind(R.id.show_music_op_spn)
    Spinner mSPNOpType;
    @Bind(R.id.show_music_toolbar_include)
    Toolbar mToolbar;
    @Bind(R.id.layout_title_tv)
    TextView mTvTitle;

    private String songMenuName, activityType;
    private int operationType;
    private long songMenuId;
    private MusicRVAdapter mMusicRVAdapter;
    private List<MusicInfo> allMusicList;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_music);
        ButterKnife.bind(this);
        initVar();
        initView();
        initData();
        mRealm = DBOperation.getInstance().selectRealm(String.valueOf(songMenuId));
    }

    public void initVar() {
        allMusicList = new ArrayList<>();
        songMenuName = getIntent().getStringExtra("songMenuName");
        if (null != songMenuName) {
            songMenuName = "歌单";
        }
        activityType = getIntent().getStringExtra("activityType");
        songMenuId = getIntent().getLongExtra("songMenuId", 0);
        operationType = 0;
    }

    public void initAddView() {
        mTvTips.setText(String.format(getResources().getString(R.string.tips), "添加"));
    }

    public void initRemoveView() {
        mTvTips.setText(String.format(getResources().getString(R.string.tips), "删除"));
    }

    public void initTb() {
        mTvTitle.setText(songMenuName);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMusicActivity.this.finish();
            }
        });
    }

    public void initView() {
        initTb();
        mMusicRVAdapter = new MusicRVAdapter(this, allMusicList);
        mMusicRVAdapter.setOnItemClickListener(this);
        mRvShowMusic.setAdapter(mMusicRVAdapter);
        mRvShowMusic.setLayoutManager(new LinearLayoutManager(this));

        mSPNOpType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                operationType = position;
                Log.e("spinner", position + "");
                switch (position) {
                    case 0:
                        loadAllMusic();
                        initAddView();
                        break;
                    case 1:
                        loadMenuListMusic();
                        initRemoveView();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void initData() {
        if ("create".equals(activityType)) {
            mSPNOpType.setSelection(0);
        } else if ("show".equals(activityType)) {
            mSPNOpType.setSelection(1);
        } else if ("default".equals(activityType)){
            mRlayoutAdd.setVisibility(View.GONE);
            operationType = 2;
            loadAllMusic();
        } else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadAllMusic() {
        Log.e("load", "loadAllMusic");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            MusicAsyncTask task = new MusicAsyncTask(this,this, Constant.SHOW_MUSIC);
            task.execute();
        }
    }

    public void loadMenuListMusic() {
        Log.e("load", "loadMenuListMusic");
        RealmResults<MusicInfo> list = DBOperation.getInstance().selectRealm(String.valueOf(songMenuId))
                .where(MusicInfo.class).findAll();
        Toast.makeText(this, list.size() + "首歌曲", Toast.LENGTH_SHORT).show();
        allMusicList.clear();
        for (MusicInfo info : list) {
            allMusicList.add(info);
        }
        mMusicRVAdapter.notifyDataSetChanged();
    }

    public void deleteMusicInfo(MusicInfo detail, int position) {
        //找出数据
        final MusicInfo deleteItem = mRealm.where(MusicInfo.class)
                .equalTo("unique", detail.getUnique())
                .findFirst();
        //删除数据
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                deleteItem.deleteFromRealm();
            }
        });
        allMusicList.remove(position);
        mMusicRVAdapter.notifyDataSetChanged();
        Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemWithParamClick(View v, Object... objects) {
        final MusicInfo detail = (MusicInfo)objects[0];
        final int position = (int)objects[1];
        switch (operationType) {
            case 0:
                RealmResults<MusicInfo> list = mRealm.where(MusicInfo.class).findAll();
                if (!BaseUtil.getInstance().isMusicInfoAdded(list, detail)) {
//                    selectMusicList.add(detail);
                    //写入数据
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(detail);
                        }
                    });
                    Toast.makeText(this, "已添加", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "歌曲已存在", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
               new AlertDialog.Builder(this)
                       .setTitle("从歌单中删除该歌曲吗？")
                       .setNegativeButton("取消", null)
                       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               deleteMusicInfo(detail, position);
                           }
                       })
                       .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemWithParamLongClick(View v, Object... objects) {

    }

    @Override
    public void result(Object object, int tag) {
        switch (tag) {
            case Constant.SHOW_MUSIC:
                if (null != object) {
                    List<MusicInfo> list = (List<MusicInfo>)object;
                    allMusicList.clear();
                    for (MusicInfo detail : list) {
                        allMusicList.add(detail);
                    }
                    mMusicRVAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    MusicAsyncTask task = new MusicAsyncTask(this, this, Constant.SHOW_MUSIC);
                    task.execute();
                } else {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRealm != null) {
            mRealm.close();
        }
    }
}
