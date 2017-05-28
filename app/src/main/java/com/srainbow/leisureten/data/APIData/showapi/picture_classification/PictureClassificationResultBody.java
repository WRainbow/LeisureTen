package com.srainbow.leisureten.data.APIData.showapi.picture_classification;

import java.util.List;

/**
 * Created by SRainbow on 2017/5/12.
 */

    public class PictureClassificationResultBody {

        //返回代码
        private int ret_code;

        //分类列表
        private List<Classification> list;

    public PictureClassificationResultBody(int ret_code, List<Classification> list) {
        this.ret_code = ret_code;
        this.list = list;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public void setList(List<Classification> list) {
        this.list = list;
    }

    public List<Classification> getList() {
        return list;
    }
}
