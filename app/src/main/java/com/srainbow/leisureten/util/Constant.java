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

    public static final String PHONE_IP = "http://192.168.42.199:8080/";
    public static final String PHONE_IP1 = "http://192.168.42.124:8080/";
    public static final String PHONE_IP2 = "http://192.168.42.93:8080/";
    public static final String REQUEST_URL= "LeisureTen/MultiServlet";

    public static final String BASERURL_JUHU_RANDOM = "https://v.juhe.cn/";
    public static final String BASERURL_JUHU = "http://japi.juhe.cn/joke/";
    public static final String JUHE_KEY = "b3c10341bc734b752fa7cb47b1fb0641";
    public static final String ADDRESS_PICJUMBO = "https://picjumbo.com/";
    public static final String SHOWAPI_APPID = "34505";
    public static final String SHOWAPI_SIGN = "5f5174a8dada496e8b10ad2aabca5b15";
    public static final String BASEURL_PICCLASSIFICATIONURL = "http://route.showapi.com/";

    public static  int PLAY_STATUS=1;      //默认播放状态为顺序播放(1:顺序、2:随机、3:单曲)
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

    public static final String USERNAME_PATTERN = "^[a-zA-Z]\\w{1,14}$";
    public static final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{5,15}$";

    public static final String DEFAULT_SONG_MENU = "手机歌曲";
    public static final String songMenuNameTable = "menuList";
    public static final String SP_USERNAME_NAME = "userName";
    public static final String SP_SETTING_NAME = "setting";
    public static final String SETTING_SONG_MENU = "songMenu";

    public static final int LOGIN_TAG = 1;
    public static final int REGISTER_TAG = 2;
    public static final int PICTURE_COLLECTION_TAG = 3;
    public static final int JOKE_COLLECTION_TAG = 4;
    public static final int ATLAS_COLLECTION_TAG = 5;
    public static final int PICTURE_COLLECTION_CANCEL_TAG = 6;
    public static final int JOKE_COLLECTION_CANCEL_TAG = 7;
    public static final int ATLAS_COLLECTION_CANCEL_TAG = 8;
    public static final int GET_JOKE_COLLECTION = 9;
    public static final int GET_PICTURE_COLLECTION = 10;
    public  static final int GET_ATLAS_COLLECTION = 11;
    public  static final int SHOW_MUSIC = 11;
}
