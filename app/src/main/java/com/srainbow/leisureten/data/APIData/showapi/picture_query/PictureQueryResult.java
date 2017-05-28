package com.srainbow.leisureten.data.APIData.showapi.picture_query;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class PictureQueryResult {

    //返回标志
    private int showapi_res_code;

    //错误信息
    private String showapi_res_error;

    //返回数据
    private PictureQueryResultBody showapi_res_body;

    public PictureQueryResult(int showapi_res_code, String showapi_res_error, PictureQueryResultBody shoapi_res_body) {
        this.showapi_res_code = showapi_res_code;
        this.showapi_res_error = showapi_res_error;
        this.showapi_res_body = shoapi_res_body;
    }

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public PictureQueryResultBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public void setShowapi_res_body(PictureQueryResultBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
}
