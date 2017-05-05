package com.srainbow.leisureten.data.APIData;

/**
 * Created by SRainbow on 2017/4/10.
 */

public class TagData {
    private int code = -1;//默认失败
    private TagDetail data;


    public TagData(){}

    public TagData(int code, TagDetail data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public TagDetail getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(TagDetail data) {
        this.data = data;
    }


}
