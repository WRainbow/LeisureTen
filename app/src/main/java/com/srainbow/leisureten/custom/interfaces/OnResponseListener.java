package com.srainbow.leisureten.custom.interfaces;

import org.json.JSONObject;

/**
 * Created by SRainbow on 2017/5/25.
 */

public interface OnResponseListener {
    void result(JSONObject result, int tag);
}
