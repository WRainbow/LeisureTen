package com.srainbow.leisureten.custom.retrofit;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by SRainbow on 2017/5/24.
 */

public class JSONResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private JSONObject jsonObject;
    JSONResponseBodyConverter(JSONObject jsonObject) {

    }
    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedReader br=new BufferedReader(value.charStream());
        String line="";
        StringBuffer buffer = new StringBuffer();
        while((line=br.readLine())!=null){
            buffer.append(line);
        }
        //buffer.toString()
        Log.d("123","接收数据:"+buffer.toString());
        try {
            jsonObject=new JSONObject(buffer.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (T)jsonObject;
    }
}