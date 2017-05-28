package com.srainbow.leisureten.data.apidata.showapi.picture_classification;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class ClassificationDetail {

    //分类id
    private String id;

    //分类名称
    private String name;

    public ClassificationDetail(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
