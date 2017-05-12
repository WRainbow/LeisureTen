package com.srainbow.leisureten.util;

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

}
