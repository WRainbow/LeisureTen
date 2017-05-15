package com.srainbow.leisureten.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

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
}
