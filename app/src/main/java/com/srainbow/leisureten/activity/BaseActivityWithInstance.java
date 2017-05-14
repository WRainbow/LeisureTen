package com.srainbow.leisureten.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivityWithInstance extends BaseActivity {

    static BaseActivityWithInstance instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void activityState(){
        instance = BaseActivityWithInstance.this;
        //deal with activity state, if need finish activityA in activityB,
        // then call ActivityA.instance.finish();
    }
}
