package com.srainbow.leisureten.data.APIData;

/**
 * Created by SRainbow on 2017/4/10.
 */

public class TagDetail {

    //标签名
    private String tag;

    //图片地址
    private String url;

    public TagDetail() {
    }

    public TagDetail(String tag, String url) {
        this.tag = tag;
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


