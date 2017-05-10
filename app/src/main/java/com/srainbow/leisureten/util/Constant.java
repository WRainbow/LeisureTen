package com.srainbow.leisureten.util;

/**
 * Created by SRainbow on 2017/4/9.
 */

public class Constant {
    private volatile static Constant instance = null;

    private Constant(){}

    public static Constant getInstance(){
        if(instance == null){
            synchronized (Constant.class){
                if(instance == null){
                    instance = new Constant();
                }
            }
        }
        return instance;
    }

    public static final String BASERURL_JUHU = "https://v.juhe.cn/";
    public static final String ADDRESS_PICJUMBO = "https://picjumbo.com/";
    public static final String SHOWAPI_APPID = "34505";
    public static final String SHOWAPI_SIGN = "5f5174a8dada496e8b10ad2aabca5b15";
    public static final String BASEURL_PICCLASSIFICATIONURL = "http://route.showapi.com/";

    public static  int PLAY_STATUS=1;      //默认播放状态为随机播放(1:顺序、2:随机、3:单曲)
    public class PlayerMsg {
        public static final int PLAY_MSG = 1;		//播放
        public static final int PAUSE_MSG = 2;		//暂停
        public static final int STOP_MSG = 3;		//停止
        public static final int CONTINUE_MSG = 4;	//继续
        public static final int PREVIOUS_MSG = 5;	//上一首
        public static final int NEXT_MSG = 6;		//下一首
        public static final int PROGRESS_CHANGE = 7;//进度改变
        public static final int PLAYING_MSG = 8;	//正在播放
    }

    public static final String CHANGE_MUSIC="com.srainbow.action.CHANGE_MUSIC";
    public static final String CHANGE_TIME="com.srainbow.action.CHANGE_TIME";
    public static final String CHANGE_PALYSTATUS="com.srainbow.action.CHANGE_PALYSTATUS";
}
