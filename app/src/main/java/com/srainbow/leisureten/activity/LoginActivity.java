package com.srainbow.leisureten.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.srainbow.leisureten.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.login_login_bt)
    Button mBnLogin;
    @Bind(R.id.login_password_et)
    EditText mEtPassword;
    @Bind(R.id.login_username_et)
    EditText mEtUsername;
    @Bind(R.id.login_toregister_tv)
    EditText mTvToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    public void activityState() {
        super.activityState();
        if(RegisterActivity.instance != null){
            RegisterActivity.instance.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //click to register
            case R.id.login_toregister_tv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            // click to login
            case R.id.login_login_bt:

                break;
        }
    }
}
