package com.srainbow.leisureten.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.custom.interfaces.OnResponseListener;
import com.srainbow.leisureten.netRequest.BackGroundRequest;
import com.srainbow.leisureten.util.BaseUtil;
import com.srainbow.leisureten.util.Constant;
import com.srainbow.leisureten.util.IdentifyingCode;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivityWithInstance implements View.OnClickListener,
        OnResponseListener{

    @Bind(R.id.register_username_et)
    EditText mEtUserName;
    @Bind(R.id.register_password_et)
    EditText mEtPassword;
    @Bind(R.id.register_password_confirm_et)
    EditText mEtConfirmPassword;
    @Bind(R.id.register_register_bt)
    Button mBnRegister;
    @Bind(R.id.register_include)
    Toolbar mToolbar;
    @Bind(R.id.layout_title_tv)
    TextView mTvTitle;
    //验证码
    @Bind(R.id.register_code_et)
    EditText mEtInputCode;
    @Bind(R.id.register_showcode_iv)
    ImageView mIvShowCode;
    @Bind(R.id.register_changecode_tv)
    TextView mTvChangeCode;

    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
    }

    public void initView(){
        initTb();

        initIdentifyingCode();

        mBnRegister.setOnClickListener(this);
        mIvShowCode.setOnClickListener(this);
        mTvChangeCode.setOnClickListener(this);
    }

    public void initTb(){
        mTvTitle.setText("注册");
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
    }

    public void initIdentifyingCode(){
        mIvShowCode.setImageBitmap(IdentifyingCode.getInstance().createBitmap());
        code = IdentifyingCode.getInstance().getCode();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //click to regist
            case R.id.register_register_bt:
                String inputCode = mEtInputCode.getText().toString();
                String userName = mEtUserName.getText().toString();
                String passWord = mEtPassword.getText().toString();
                String passWordConfirm = mEtConfirmPassword.getText().toString();
                //check password and passwordConfirm is equal

                if (inputCode.isEmpty()) {
                    showMessageByString("验证码不能为空");
                } else if (!code.toLowerCase().equals(inputCode.toLowerCase())) {
                    showMessageByString("验证码错误");
                    initIdentifyingCode();
                } else if (passWord.equals(passWordConfirm)) {
                    if (!BaseUtil.getInstance().checkUserNameInRegisterFormat(userName)) {
                        //username is not match
                        mEtUserName.setError("请检查用户名格式");
                    } else if (!BaseUtil.getInstance().checkPasswordInRegisterFormat(passWord)) {
                        //password is not match
                        mEtPassword.setError("请检查密码格式");
                    } else {
                        BackGroundRequest.getInstance().registerConfirm(this, Constant.REGISTER_TAG, userName, passWord);
                    }
                }else{
                    //password and passwordConfirm not match
                    mEtConfirmPassword.setError("请检查两次密码输入是否一致");
                }

                break;
            case R.id.register_changecode_tv:
                initIdentifyingCode();
                break;
            case R.id.register_showcode_iv:
                initIdentifyingCode();
                break;
        }
    }

    @Override
    public void result(JSONObject result, int tag) {
        if (result != null) {
            switch (tag){
                case Constant.REGISTER_TAG:
                    if ("true".equals(result.optString("result"))) {
                        showMessageByString("注册成功");
                        RegisterActivity.this.finish();
                    } else if ("用户名已存在".equals(result.optString("result"))) {
                        showMessageByString("用户名已存在");
                    } else if ("false".equals(result.optString("result"))) {
                        showMessageByString("注册失败");
                    } else {
                        showMessageByString("未知错误");
                    }
                    break;
            }
        } else {
            showMessageByString("网络错误");
        }

    }
}
