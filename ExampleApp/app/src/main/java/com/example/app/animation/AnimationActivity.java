package com.example.app.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.R;

public class AnimationActivity extends Activity implements View.OnClickListener{

    private Button mStartButton;
    private TextView mAnimTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        initView();
        initListener();
    }

    private void initView() {
        mAnimTextView = (TextView) findViewById(R.id.tv_animation);
        mStartButton = (Button) findViewById(R.id.btn_start);
    }

    private void initListener() {
        mStartButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                // 方法一：
//                Animation anim = new RotateAnimation(0.0f, 360f);
//                anim.setInterpolator(new AccelerateDecelerateInterpolator());
//                anim.setDuration(3000);
                // 方法二：
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
                mAnimTextView.startAnimation(anim);
                break;
        }
    }
}
