package com.srainbow.leisureten.data.APIData.showapi.picture_query;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class PictureQueryResultBody {
    private int ret_code;
    private PicturePageBean pagebean;

    public PictureQueryResultBody(int ret_code, PicturePageBean pagebean) {
        this.ret_code = ret_code;
        this.pagebean = pagebean;
    }

    public int getRet_code() {
        return ret_code;
    }

    public PicturePageBean getPagebean() {
        return pagebean;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public void setPagebean(PicturePageBean pagebean) {
        this.pagebean = pagebean;
    }
}
