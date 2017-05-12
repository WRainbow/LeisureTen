package com.srainbow.leisureten.data.APIData.showapi.picture_classification;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class ClassificationDetail {
    private String id;
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
