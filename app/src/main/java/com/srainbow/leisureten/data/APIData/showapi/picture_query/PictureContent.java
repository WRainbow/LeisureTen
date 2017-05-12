package com.srainbow.leisureten.data.APIData.showapi.picture_query;

import java.util.List;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class PictureContent {
    private String typeName;
    private String title;
    private List<PictureSizeUrl> list;
    private String itemId;
    private String type;
    private String ct;

    public PictureContent(String typeName, String title, List<PictureSizeUrl> list, String itemId, String type, String ct) {
        this.typeName = typeName;
        this.title = title;
        this.list = list;
        this.itemId = itemId;
        this.type = type;
        this.ct = ct;
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

    public String getTypeName() {
        return typeName;
    }

    public String getTitle() {
        return title;
    }

    public List<PictureSizeUrl> getList() {
        return list;
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

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setList(List<PictureSizeUrl> list) {
        this.list = list;
    }
}
