package com.srainbow.leisureten.activity;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.srainbow.leisureten.util.Constant;

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

    public void saveUserNameToSP(String userName) {
        SharedPreferences sp = getSharedPreferences(Constant.SharedPreferencesName, MODE_PRIVATE);
        sp.edit().putString("userName", userName).apply();
    }

    public String getUserNameFromSP() {
        SharedPreferences sp = getSharedPreferences(Constant.SharedPreferencesName, MODE_PRIVATE);
        return sp.getString("userName", "null");
    }
}
