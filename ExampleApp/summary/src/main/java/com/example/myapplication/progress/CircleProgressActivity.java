package com.example.myapplication.progress;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class CircleProgressActivity extends Activity {

    private static final String TAG = "CircleProgressActivity";

    private CircleProgressBar mCircleProgressBar;
    private Button mButton;
    private ObjectAnimator mObjectAnimator;
    private boolean mJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_progress);
        mCircleProgressBar = (CircleProgressBar) findViewById(R.id.circle_pb);
        mButton = (Button) findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJump = true;
                if (mObjectAnimator.getAnimatedFraction() > 0.9f) {
                    mObjectAnimator.cancel();
                }
                Log.d(TAG, "fraction: " + mObjectAnimator.getAnimatedFraction());
                Log.d(TAG, "fraction: " + mObjectAnimator.getCurrentPlayTime());
            }
        });

        startProgress();
    }

    private void startProgress() {
        mObjectAnimator = ObjectAnimator.ofInt(mCircleProgressBar, "progress", 0, 101);
        mObjectAnimator.setDuration(1000);
        mObjectAnimator.setRepeatCount(30);
        mObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "====onAnimationStart===");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "====onAnimationEnd===");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                Log.d(TAG, "====onAnimationCancel===");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                Log.d(TAG, "====onAnimationRepeat===");
            }
        });
        mObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mJump && animation.getAnimatedFraction() > 0.9f) {
                    animation.cancel();
                }
            }
        });

        mObjectAnimator.start();
    }

}
