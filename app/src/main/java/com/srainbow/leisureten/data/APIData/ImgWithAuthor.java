package com.srainbow.leisureten.data.APIData;

/**
 * Created by SRainbow on 2017/4/9.
 */

public class ImgWithAuthor {

    //有下一页时nextPage为1，否则为0
    private int nextPage;

    //有上一页时prePage为1，否则为0
    private int prePage;

    //下一页url
    private String nextUrl;

    //上一页url
    private String preUrl;

    //图片地址
    private String imgUrl;

    //图片作者
    private String imgAuthor;

    public ImgWithAuthor(){}

    public ImgWithAuthor(String imgUrl, String imgAuthor, int next, int pre) {
        this.imgUrl = imgUrl;
        this.imgAuthor = imgAuthor;
        this.nextPage = next;
        this.prePage = pre;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public String getPreUrl() {
        return preUrl;
    }

    public int getNextPage() {

        return nextPage;
    }

    public int getPrePage() {
        return prePage;
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

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public void setPreUrl(String preUrl) {
        this.preUrl = preUrl;
    }
}
