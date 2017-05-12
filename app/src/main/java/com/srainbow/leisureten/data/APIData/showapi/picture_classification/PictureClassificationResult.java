package com.srainbow.leisureten.data.APIData.showapi.picture_classification;

/**
 * Created by SRainbow on 2017/5/12.
 */

public class PictureClassificationResult {
    private int showapi_res_code;
    private String showapi_res_error;
    private PictureClassificationResultBody showapi_res_body;

    public PictureClassificationResult(int showapi_res_code, String showapi_res_error, PictureClassificationResultBody showapi_res_body) {
        this.showapi_res_code = showapi_res_code;
        this.showapi_res_error = showapi_res_error;
        this.showapi_res_body = showapi_res_body;
    }

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public PictureClassificationResultBody getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public void setShowapi_res_body(PictureClassificationResultBody showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }
}
