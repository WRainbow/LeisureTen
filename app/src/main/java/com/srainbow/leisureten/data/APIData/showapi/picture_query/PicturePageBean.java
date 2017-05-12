package com.srainbow.leisureten.data.APIData.showapi.picture_query;

import java.util.List;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class PicturePageBean {
    private int allPages;
    private List<PictureContent> contentlist;
    private int currentPage;
    private int allNum;
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
