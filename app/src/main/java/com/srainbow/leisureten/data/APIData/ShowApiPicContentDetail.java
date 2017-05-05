package com.srainbow.leisureten.data.APIData;

import java.util.List;

/**
 * Created by SRainbow on 2017/4/19.
 */

public class ShowApiPicContentDetail {

    public String ret_code;
    public PageBean pagebean;

    public ShowApiPicContentDetail() {

    }

    public ShowApiPicContentDetail(String ret_code, PageBean pagebean) {

        this.ret_code = ret_code;
        this.pagebean = pagebean;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public void setPagebean(PageBean pagebean) {
        this.pagebean = pagebean;
    }

    public String getRet_code() {

        return ret_code;
    }

    public PageBean getPagebean() {
        return pagebean;
    }

    public class PageBean{
        String allPages;
        List<PicContent> contentlist;
        String currentPage;
        String allNum;

        public void setAllPages(String allPages) {
            this.allPages = allPages;
        }

        public void setContentlist(List<PicContent> contentlist) {
            this.contentlist = contentlist;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public void setAllNum(String allNum) {
            this.allNum = allNum;
        }

        public void setMaxResult(String maxResult) {
            this.maxResult = maxResult;
        }

        public String getAllPages() {

            return allPages;
        }

        public List<PicContent> getContentlist() {
            return contentlist;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public String getAllNum() {
            return allNum;
        }

        public String getMaxResult() {
            return maxResult;
        }

        String maxResult;
    }
    public class PicContent{
        String typeName;
        String title;
        List<PicSizeWithUrl> list;
        String itemId;
        String type;
        String ct;

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setList(List<PicSizeWithUrl> list) {
            this.list = list;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setCt(String ct) {
            this.ct = ct;
        }

        public String getTypeName() {

            return typeName;
        }

        public String getTitle() {
            return title;
        }

        public List<PicSizeWithUrl> getList() {
            return list;
        }

        public String getItemId() {
            return itemId;
        }

        public String getType() {
            return type;
        }

        public String getCt() {
            return ct;
        }
    }
    public class PicSizeWithUrl{
        String big;
        String small;
        String middle;

        public void setBig(String big) {
            this.big = big;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public void setMiddle(String middle) {
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
    }

}
