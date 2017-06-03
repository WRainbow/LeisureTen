package com.srainbow.leisureten.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.netRequest.BackGroundRequest;
import com.srainbow.leisureten.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener,
        OnResponseListener{

    @Bind(R.id.login_login_bt)
    Button mBnLogin;
    @Bind(R.id.login_password_et)
    EditText mEtPassword;
    @Bind(R.id.login_username_et)
    EditText mEtUsername;
    @Bind(R.id.login_toregister_tv)
    TextView mTvToRegister;
    @Bind(R.id.login_tb)
    Toolbar mTbLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void activityState() {
        super.activityState();
//        if(RegisterActivity.instance != null){
//            RegisterActivity.instance.finish();
//        }
    }

    public void initView(){
        mTbLogin.setNavigationIcon(R.drawable.ic_back);
        mTbLogin.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("test", "test");
                LoginActivity.this.finish();
            }
        });

        mTvToRegister.setOnClickListener(this);
        mBnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //click to register
            case R.id.login_toregister_tv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            // click to loginConfirm
            case R.id.login_login_bt:
                String userName = mEtUsername.getText().toString();
                String passWord = mEtPassword.getText().toString();
                if(!"".equals(userName) && !"".equals(passWord)) {
                    BackGroundRequest.getInstance().loginConfirm(this, Constant.LOGIN_TAG, userName, passWord);
                }else{
                    showMessageByString("用户名或密码不能为空");
                }
                break;
        }
    }

    @Override
    public void result(Object object, int tag) {
        if(null != object) {
            try{
                JSONObject result = new JSONObject((String)object);
                switch (tag){
                    case Constant.LOGIN_TAG:
                        if ("true".equals(result.optString("result"))) {
                            saveUserNameToSP(mEtUsername.getText().toString());
                            showMessageByString("登录成功");
                            Intent intent = new Intent();
                            intent.putExtra("userName", mEtUsername.getText().toString());
                            setResult(RESULT_OK, intent);
                            LoginActivity.this.finish();
                        } else {
                            showMessageByString("用户名或密码错误");
                        }
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            showMessageByString("网络错误");
        }
    }
}
