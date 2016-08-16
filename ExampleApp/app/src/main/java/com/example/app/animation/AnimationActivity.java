package com.example.app.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;


/**
 * 1. Animation只是重绘了动画，View的实际位置没有发生变化(点击tv原来的位置，弹出toast)，不适用于交互的组件
 * 2. ObjectAnimator真正改变了控件的位置
 * */
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
        mAnimTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                // TODO: RotateAnimation使用
                // 方法一：
//                Animation anim = new RotateAnimation(0.0f, 360f);
//                anim.setInterpolator(new AccelerateDecelerateInterpolator());
//                anim.setDuration(3000);
                // 方法二：
//                Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate);
//
                // TODO: Animation只是重绘了动画，View的实际位置没有发生变化，不适用于交互的组件
//                TranslateAnimation anim = new TranslateAnimation(0, 200, 0, 100);
//                anim.setDuration(3000);
//                anim.setFillBefore(false); //
//                mAnimTextView.startAnimation(anim);
                // TODO: ObjectAnimator 多个动画一起同时开始
//                ObjectAnimator.ofFloat(mAnimTextView, "rotation", 0, 360F).setDuration(1000).start();
//                ObjectAnimator.ofFloat(mAnimTextView, "translationX", 0, 200F).setDuration(1000).start();
//                ObjectAnimator.ofFloat(mAnimTextView, "translationY", 0, 200F).setDuration(1000).start();

                // TODO:  PropertyValuesHolder优化了多个属性动画一起使用时的更加节省内存，
//                PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("rotation", 0, 360F);
//                PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("translationX", 0, 200F);
//                PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("translationY", 0, 200F);
//                ObjectAnimator.ofPropertyValuesHolder(mAnimTextView, holder1, holder2, holder3).setDuration(1000).start();

                // TODO: AnimatorSet 动画可以灵活组合
//                ObjectAnimator animator1 = ObjectAnimator.ofFloat(mAnimTextView, "rotation", 0, 360F);
//                ObjectAnimator animator2 = ObjectAnimator.ofFloat(mAnimTextView, "translationX", 0, 200F);
//                ObjectAnimator animator3 = ObjectAnimator.ofFloat(mAnimTextView, "translationY", 0, 200F);
//                AnimatorSet set = new AnimatorSet();
////                set.playSequentially(animator1, animator2, animator3); // 按顺序执行
//                set.play(animator2).with(animator3);
//                set.play(animator2).after(animator1);
//                set.setDuration(1000).start();

                break;
            case R.id.tv_animation:
                Toast.makeText(this, "click textview", Toast.LENGTH_SHORT).show();
                // TODO: animator动画监听
//                ObjectAnimator animator = ObjectAnimator.ofFloat(mAnimTextView, "alpha", 0, 1);
//                // AnimatorListenerAdapter实现了接口Animator.AnimatorListener
//                animator.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                    }
//                });
//                animator.setDuration(1000).start();

                break;
        }
    }
}
