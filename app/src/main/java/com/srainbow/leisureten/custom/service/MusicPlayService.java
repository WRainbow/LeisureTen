package com.srainbow.leisureten.custom.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.srainbow.leisureten.data.realm.MusicInfo;
import com.srainbow.leisureten.data.realm.SongMenu;
import com.srainbow.leisureten.util.BaseUtil;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.DBOperation;
import com.srainbow.leisureten.util.FileOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private int currentMusicPosition;
    private int msg;
    private boolean isPause;
    private String menuName;
    private Context mContext;
    private List<MusicInfo> musicDetailList = new ArrayList<>();

    public MusicPlayService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service", "onCreate() executed");
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);

        mContext = getApplicationContext();
        menuName = FileOperation.getInstance().getSettingValueToSP(mContext,
                Constant.SP_SETTING_NAME, Constant.SETTING_SONG_MENU);
        getMusicList(menuName);
        Log.e("musicDetailListSize", musicDetailList.size() + "");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = FileOperation.getInstance().getSettingValueToSP(mContext,
                Constant.SP_SETTING_NAME, Constant.SETTING_SONG_MENU);
        if (!menuName.equals(name) && !"null".equals(name)) {
            menuName = name;
            getMusicList(name);
        }
        Bundle bundle=intent.getExtras();
        currentMusicPosition = 0;
        msg=bundle.getInt("MSG");
        Log.e("MusicService", "msg = " + msg);
        switch (msg){
            case Constant.PlayerMsg.PLAY_MSG:
                playMusic(currentMusicPosition);
                break;
            case Constant.PlayerMsg.PAUSE_MSG:
                pauseMusic();
                break;
            case Constant.PlayerMsg.STOP_MSG:
                stopMusic();
                break;
            case Constant.PlayerMsg.CONTINUE_MSG:
                resume();
                break;
            case Constant.PlayerMsg.PREVIOUS_MSG:
                playMusic(currentMusicPosition);
                break;
            case Constant.PlayerMsg.NEXT_MSG:
                playMusic(currentMusicPosition);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void playMusic(int musicPosition){
        Log.e("MusicPlayService", "playMusic");
        if (musicDetailList.size() > 0) {
            if(musicPosition >= 0){
                Log.e("size", musicDetailList.size() + "");
                String path=musicDetailList.get(musicPosition).getPath();
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(path);
                    mediaPlayer.prepare();  //进行缓冲
                    mediaPlayer.start();
//            mediaPlayer.setOnPreparedListener(new PreparedListener(position));//注册一个监听器
                }catch (IOException e){
                    Log.e("error",e.getMessage());
                }
            }
        } else {
            Toast.makeText(mContext, "歌单中没有任何歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseMusic() {
        Log.e("MusicPlayService", "pauseMusic");
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    private void resume() {
        Log.e("MusicPlayService", "resume");
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    private void stopMusic(){
        Log.e("MusicPlayService", "stopMusic");
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getMusicList(String name) {
        if (Constant.DEFAULT_SONG_MENU.equals(name)) {
            musicDetailList = FileOperation.getInstance().getLocalMusic(mContext);
        } else if ("null".equals(name)) {
            Toast.makeText(mContext, "歌单不存在，请重新设置播放歌单", Toast.LENGTH_SHORT).show();
        } else {
            musicDetailList = FileOperation.getInstance().getSongMenuMusic(mContext, menuName);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mp==mediaPlayer){
            switch (Constant.PLAY_STATUS){
                //默认顺序播放
                case 1:
                    currentMusicPosition++;
                    if(currentMusicPosition > musicDetailList.size()-1) {	//变为第一首的位置继续播放
                        currentMusicPosition = 0;
                    }
                    playMusic(currentMusicPosition);
                    break;
                //随机播放
                case 2:
                    currentMusicPosition= BaseUtil.getInstance().getRandomIndex(musicDetailList.size()-1);
                    playMusic(currentMusicPosition);
                    break;
                //单曲循环
                case 3:
                    mediaPlayer.start();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.reset();//不加这句会logcat出：mediaplayer went away with unhandled events
            mediaPlayer.release();
        }
    }
}
