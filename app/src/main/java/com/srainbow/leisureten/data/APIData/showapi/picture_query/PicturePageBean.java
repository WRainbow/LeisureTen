package com.srainbow.leisureten.data.apidata.showapi.picture_query;

import java.util.List;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class PicturePageBean {

    //总页数
    private int allPages;

    //图集列表
    private List<PictureContent> contentlist;

    //当前页
    private int currentPage;

    //图集总数
    private int allNum;

    //每页图集最大数
    private int maxResult;

    public PicturePageBean(int allPages, List<PictureContent> contentlist, int currentPage, int allNum, int maxResult) {
        this.allPages = allPages;
        this.contentlist = contentlist;
        this.currentPage = currentPage;
        this.allNum = allNum;
        this.maxResult = maxResult;
    }

    public int getAllPages() {
        return allPages;
    }

    public List<PictureContent> getContentlist() {
        return contentlist;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getAllNum() {
        return allNum;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public void setContentlist(List<PictureContent> contentlist) {
        this.contentlist = contentlist;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }
}
