package com.srainbow.leisureten.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.util.BaseUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivityWithInstance implements View.OnClickListener{

    @Bind(R.id.register_username_et)
    EditText mEtUserName;
    @Bind(R.id.register_password_et)
    EditText mEtPassword;
    @Bind(R.id.register_password_confirm_et)
    EditText mEtConfirmPassword;
    @Bind(R.id.register_register_bt)
    Button mBnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    public void initView(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //click to regist
            case R.id.register_register_bt:
                String userName = mEtUserName.getText().toString();
                String passWord = mEtPassword.getText().toString();
                String passWordConfirm = mEtConfirmPassword.getText().toString();
                //check password and passwordConfirm is equal
                if(passWord.equals(passWordConfirm)){
                    //check userName's format
                    if(BaseUtil.getInstance().checkUserNameInRegisterFormat(userName) &&
                            BaseUtil.getInstance().checkPasswordInRegisterFormat(passWord)){
                        showMessageByString("注册成功请返回登陆");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else if(!BaseUtil.getInstance().checkUserNameInRegisterFormat(userName)){
                        //username is not match
                        mEtUserName.setError("请检查用户名格式");
                    }else if(!BaseUtil.getInstance().checkPasswordInRegisterFormat(passWord)){
                        //password is not match
                        mEtPassword.setError("请检查密码格式");
                    }
                }else{
                    //password and passwordConfirm not match
                    mEtConfirmPassword.setError("请检查两次密码输入是否一致");
                }

                break;
        }
    }
}
