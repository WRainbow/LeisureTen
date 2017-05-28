package com.srainbow.leisureten.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.srainbow.leisureten.util.Constant;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public BaseFragment() {
    }

    public static BaseFragment newInstance(){
        return new BaseFragment();
    }

    public void showMessageByString(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showAndHideView(View toShow, View toHide){
        toShow.setVisibility(View.VISIBLE);
        toHide.setVisibility(View.GONE);
    }

    public void saveUserNameToSP(Context context, String userName) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SharedPreferencesName, MODE_PRIVATE);
        sp.edit().putString("userName", userName).apply();
    }

    public String getUserNameFromSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constant.SharedPreferencesName, MODE_PRIVATE);
        return sp.getString("userName", "null");
    }
}
