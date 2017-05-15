package com.srainbow.leisureten.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityState();
    }

    public void showMessageByString(@NonNull String msg){
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void activityState(){
        //deal with activity state, if need finish activityA in activityB,
        // then call ActivityA.instance.finish();
    }

    public void showAndHideView(View toShow, View toHide){
        toShow.setVisibility(View.VISIBLE);
        toHide.setVisibility(View.GONE);
    }
}
