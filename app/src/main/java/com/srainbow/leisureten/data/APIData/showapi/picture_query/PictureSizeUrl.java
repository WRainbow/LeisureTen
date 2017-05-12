package com.srainbow.leisureten.data.APIData.showapi.picture_query;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class PictureSizeUrl {
    private String big;
    private String small;
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
