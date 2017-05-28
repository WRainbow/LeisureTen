package com.srainbow.leisureten.data.APIData;

/**
 * Created by SRainbow on 2016/10/10.
 */
public class FunnyPicDetail {

    //描述
    public String content;

    //哈希值
    public String hashId;

    //时间戳
    public String unixtime;

    //更新时间
    public String updatetime;

    //图片地址
    public String url;

    //是否已收藏
    public boolean isCollected = false;

    public boolean isCollected() {
        return isCollected;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUpdatetime() {
        return updatetime;
    }
}
