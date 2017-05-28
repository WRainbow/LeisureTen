package com.srainbow.leisureten.data.apidata.juhe.joke;

/**
 * Created by SRainbow on 2016/10/10.
 */
public class JokeDetail {


    //文字内容
    public String content;

    //哈希值
    public String hashId;

    //时间戳
    public String unixtime;

    //更新时间
    public  String updatetime;

    //是否已收藏
    public boolean isCollected = false;

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(String unixtime) {
        this.unixtime = unixtime;
    }
}
