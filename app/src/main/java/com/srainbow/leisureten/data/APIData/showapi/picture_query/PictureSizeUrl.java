package com.srainbow.leisureten.data.apidata.showapi.picture_query;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class PictureSizeUrl {

    //大图地址
    private String big;

    //小图地址
    private String small;

    //中图地址
    private String middle;

    public PictureSizeUrl(String big, String small, String middle) {
        this.big = big;
        this.small = small;
        this.middle = middle;
    }

    public String getBig() {
        return big;
    }

    public String getSmall() {
        return small;
    }

    public String getMiddle() {
        return middle;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }
}
