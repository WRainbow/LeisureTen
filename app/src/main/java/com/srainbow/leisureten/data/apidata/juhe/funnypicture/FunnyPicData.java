package com.srainbow.leisureten.data.apidata.juhe.funnypicture;

/**
 * Created by SRainbow on 2016/10/10.
 */
public class FunnyPicData {

    //返回值
    public String reason;

    //返回结果
    public FunnyPicResult result;

    //错误代码
    public String error_code;

    public FunnyPicData(String reason, FunnyPicResult result, String error_code) {
        this.reason = reason;
        this.result = result;
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public FunnyPicResult getResult() {
        return result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResult(FunnyPicResult result) {
        this.result = result;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
