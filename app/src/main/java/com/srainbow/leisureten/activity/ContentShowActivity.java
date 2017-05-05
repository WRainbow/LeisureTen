package com.srainbow.leisureten.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.srainbow.leisureten.R;
import com.srainbow.leisureten.fragment.JokeFragment;
import com.srainbow.leisureten.fragment.PictureFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentShowActivity extends BaseActivity {

    private static String classification = "";
    private FragmentTransaction mTransaction;
    private PictureFragment mPictureFragment;
    private JokeFragment mJokeFragment;

    @Bind(R.id.content_refresh_failed_prompt_tv)
    TextView mTvRefreshFailedPrompt;
    @Bind(R.id.content_loadmore_failed_prompt_tv)
    TextView mTvLoadMoreFailedPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_show);
        ButterKnife.bind(this);
        initParameter();
        initViews();
    }

    public void initParameter(){
        classification = getIntent().getStringExtra("classificationName");
    }

    public void initViews(){
        switch (classification){
            case "趣图":
                mPictureFragment = PictureFragment.newInstance();
                mTransaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_showfragment_fl, mPictureFragment);
                mTransaction.commit();
                break;
            case "笑话":
                mJokeFragment = JokeFragment.newInstance();
                mTransaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_showfragment_fl, mJokeFragment);
                mTransaction.commit();
                break;
        }
    }

    public void showRefreshFailedPrompt(){
        mTvRefreshFailedPrompt.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTvRefreshFailedPrompt, "alpha", 1f, 0f);
        animator.setDuration(3500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTvRefreshFailedPrompt.setVisibility(View.GONE);
            }
        });
    }

    public void showLoadMoreFailedPrompt(){
        mTvLoadMoreFailedPrompt.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTvLoadMoreFailedPrompt, "alpha", 1f, 0f);
        animator.setDuration(2500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mTvLoadMoreFailedPrompt.setVisibility(View.GONE);
            }
        });
    }

}
