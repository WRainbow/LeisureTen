package com.srainbow.leisureten.data.APIData;

/**
 * Created by SRainbow on 2016/10/10.
 */
public class JokeDetail {
    public String content;
    public String hashId;
    public String unixtime;
    public boolean isCollected = false;

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
