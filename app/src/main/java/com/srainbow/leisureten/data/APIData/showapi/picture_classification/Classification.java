package com.srainbow.leisureten.data.apidata.showapi.picture_classification;

import java.util.List;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class Classification {

    //分类类型
    private String name;

    //详细分类列表
    private List<ClassificationDetail> list;

    public Classification(String name, List<ClassificationDetail> list) {
        this.name = name;
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<ClassificationDetail> list) {
        this.list = list;
    }

    public List<ClassificationDetail> getList() {
        return list;
    }
}
