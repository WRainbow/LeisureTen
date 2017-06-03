package com.srainbow.leisureten.netRequest;

import android.content.Context;
import android.os.AsyncTask;

import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.data.realm.MusicInfo;
import com.srainbow.leisureten.util.FileOperation;

import java.util.List;


/**
 * Created by SRainbow on 2017/6/1.
 */

public class MusicAsyncTask extends AsyncTask<String, Integer, List<MusicInfo>> {

    private Context mContext;
    private OnResponseListener responseListener;
    private int tag;

    public MusicAsyncTask(Context context, OnResponseListener listener, int tag) {
        this.mContext = context;
        this.responseListener = listener;
        this.tag = tag;
    }

    @Override
    protected List<MusicInfo> doInBackground(String... params) {
        return FileOperation.getInstance().getLocalMusic(mContext);
    }

    @Override
    protected void onPostExecute(List<MusicInfo> musicDetails) {
        super.onPostExecute(musicDetails);
        if(responseListener != null) {
            responseListener.result(musicDetails, tag);
        }
    }
}
