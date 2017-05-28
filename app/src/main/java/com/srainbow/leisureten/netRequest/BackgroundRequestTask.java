package com.srainbow.leisureten.netRequest;

import android.os.AsyncTask;
import android.util.Log;

import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by SRainbow on 2017/5/24.
 */

public class BackgroundRequestTask extends AsyncTask<String, Integer, String> {

    private OnResponseListener onResponseListener;
    private int tag;
    private JSONObject resultJson;
    private StringBuilder requestString = new StringBuilder();

    public BackgroundRequestTask(OnResponseListener listener, int tag){
        this.onResponseListener = listener;
        this.tag = tag;
    }

    public void setParam(String key, String value){
        try {
            value  = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestString.append(key).append("=").append(value).append("&");
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData="";
        try {
            URL url = new URL(Constant.PHONE_IP2 + Constant.REQUEST_URL); //URL对象
            conn = (HttpURLConnection)url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            if(requestString.length() > 0){
                //删除多余的&符号
                requestString.deleteCharAt(requestString.length() - 1);

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.write(requestString.toString().getBytes());
                out.flush();
                out.close();
            }
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine="";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine;
            }
        }catch (IOException e) {
            //Log.i("IO error", e.getMessage());
            e.printStackTrace();

        }finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
            if(conn != null){
                conn.disconnect();
            }
        }
        Log.e("Task", resultData);
        return resultData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            resultJson = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        onResponseListener.result(resultJson, tag);

    }
}
