package com.srainbow.leisureten.custom.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import com.srainbow.leisureten.data.BaseData.MusicDetail;
import com.srainbow.leisureten.util.BaseUtil;
import com.srainbow.leisureten.util.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private int currentMusicPosition;
    private int msg;
    private boolean isPause;
    private MyReceiver myReceiver;
    private List<MusicDetail> musicDetailList = new ArrayList<>();

    public MusicPlayService(final Context context) {
        new Runnable() {
            @Override
            public void run() {
                musicDetailList = getLocalMusic(context);
            }
        }.run();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("service", "onCreate() executed");
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        registerReceiver(myReceiver,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle=intent.getExtras();
        currentMusicPosition =bundle.getInt("currentPosition");
        msg=bundle.getInt("MSG");
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
        if(musicPosition > 0){
            String path=musicDetailList.get(musicPosition).path;
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
    }

    private void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
        }
    }

    private void resume() {
        if (isPause) {
            mediaPlayer.start();
            isPause = false;
        }
    }

    private void stopMusic(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                    Intent sendIntent = new Intent(Constant.CHANGE_MUSIC);
                    sendIntent.putExtra("currentPosition", currentMusicPosition);
                    // 发送广播，将被Activity组件中的BroadcastReceiver接收到
                    sendBroadcast(sendIntent);
                    playMusic(currentMusicPosition);
                    break;
                //随机播放
                case 2:
                    currentMusicPosition= BaseUtil.getRandomIndex(musicDetailList.size()-1);
                    Intent intent=new Intent(Constant.CHANGE_MUSIC);
                    intent.putExtra("currentPosition",currentMusicPosition);
                    sendBroadcast(intent);
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

    /**
     *
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     *
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int positon;

        public PreparedListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();    //开始播放
            if(positon > 0) {    //如果音乐不是从头播放
                mediaPlayer.seekTo(positon);
            }
        }
    }
    public static List<MusicDetail> getLocalMusic(Context context){
        List<MusicDetail> musicDetails=new ArrayList<>();
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if(cursor!=null) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    MusicDetail musicDetail = new MusicDetail();
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
                        Uri albumUri= ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumId);
                        musicDetail.setAlbumUri(albumUri);
                        musicDetail.setBaseInfo(title, artist, duration, size, path);
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

    /**
     * handler用来接收消息，来发送广播更新播放时间
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if(mediaPlayer != null) {
                    int currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                    Intent intent = new Intent();
                    intent.setAction(Constant.CHANGE_TIME);
                    intent.putExtra("currentTime", currentTime);
                    sendBroadcast(intent); // 给PlayerActivity发送广播
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        };
    };

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("playStutas", -1);
            switch (control) {
                case 1:
                    Constant.PLAY_STATUS = 1; // 将播放状态置为1表示：顺序播放
                    break;
                case 2:
                    Constant.PLAY_STATUS = 2;	//将播放状态置为2表示：随机播放
                    break;
                case 3:
                    Constant.PLAY_STATUS = 3;	//将播放状态置为3表示：单曲循环
                    break;
            }

        }
    }
}
