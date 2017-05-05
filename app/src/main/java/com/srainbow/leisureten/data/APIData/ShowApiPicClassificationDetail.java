package com.srainbow.leisureten.data.APIData;

import java.util.List;

/**
 * Created by SRainbow on 2017/4/13.
 */

public class ShowApiPicClassificationDetail {
    public String  ret_code;
    public List<AllClassification> list;

    public ShowApiPicClassificationDetail() {
    }

    public ShowApiPicClassificationDetail(String ret_code, List<AllClassification> list) {

        this.ret_code = ret_code;
        this.list = list;
    }

    public String getRet_code() {
        return ret_code;
    }

    public List<AllClassification> getList() {
        return list;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public void setList(List<AllClassification> list) {
        this.list = list;
    }

    public class Classification{
        public String id;

        public String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class AllClassification{
        public String name;
        public List<Classification> list;

        public String getName() {
            return name;
        }

        public List<Classification> getList() {
            return list;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setList(List<Classification> list) {
            this.list = list;
        }
    }
}
