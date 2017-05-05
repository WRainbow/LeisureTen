package com.srainbow.leisureten.data.APIData;

/**
 * Created by SRainbow on 2017/4/9.
 */

public class ImgWithAuthor {
    private String imgUrl;
    private String imgAuthor;

    public ImgWithAuthor(){}

    public ImgWithAuthor(String imgUrl, String imgAuthor) {
        this.imgUrl = imgUrl;
        this.imgAuthor = imgAuthor;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getImgAuthor() {
        return imgAuthor;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setImgAuthor(String imgAuthor) {
        this.imgAuthor = imgAuthor;
    }
}
