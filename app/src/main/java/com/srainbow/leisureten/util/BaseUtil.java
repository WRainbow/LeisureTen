package com.srainbow.leisureten.util;

import com.srainbow.leisureten.data.realm.MusicInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by SRainbow on 2017/4/20.
 */

public class BaseUtil {

    private volatile static BaseUtil instance = null;

    private BaseUtil(){}

    public static BaseUtil getInstance(){
        if(instance == null){
            synchronized (BaseUtil.class){
                if(instance == null){
                    instance = new BaseUtil();
                }
            }
        }
        return instance;
    }

    public int getRandomIndex(int end) {
        return (int) (Math.random() * end);
    }

    public boolean checkUserNameInRegisterFormat(String name){
        Pattern p = Pattern.compile(Constant.USERNAME_PATTERN);
        return p.matcher(name).matches();
    }

    public boolean checkPasswordInRegisterFormat(String pwd) {
        Pattern p = Pattern.compile(Constant.PASSWORD_PATTERN);
        return p.matcher(pwd).matches();
    }

    public String formatDuration(long duration){
        return new SimpleDateFormat("mm:ss").format(duration);
    }

    public String formatSize(long size){
        float floatSize=size/1024/1024f;
        return String.valueOf((double)((int)(floatSize*10+0.5))/10)+"M";
    }

    public long getTimeStamp() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public boolean isMusicInfoAdded(List<MusicInfo> list, MusicInfo info) {
        for (MusicInfo musicInfo : list) {
            if (musicInfo.getUnique().equals(info.getUnique())) {
                return true;
            }
        }
        return false;
    }

}
