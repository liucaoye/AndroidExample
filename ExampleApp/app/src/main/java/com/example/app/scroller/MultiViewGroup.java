package com.example.app.scroller;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Author: LY
 * Date: 16/8/21
 */
public class MultiViewGroup extends ViewGroup {

    private Scroller mScroller;

    private int curScreen;
    private int mTouchSlop;

    public MultiViewGroup(Context context) {
        this(context, null);
    }

    public MultiViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void startMove() {
        curScreen++;
        // 使用动画控制偏移过程 , 3s内到位
        mScroller.startScroll((curScreen - 1) * getWidth(), 0, getWidth(), 0, 3000);
        invalidate();
    }

    public void stopMove() {
        if (mScroller != null) {

        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
//        // 如果返回true，表示动画还没结束
//        if (mScroller.computeScrollOffset()) {
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            postInvalidate();
//        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(100, 600);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int startLeft = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(startLeft, 0, startLeft + 100, 600);
            startLeft += 100;
        }
    }

    private void initView() {

        mScroller = new Scroller(getContext());

        LinearLayout oneLL = new LinearLayout(getContext());
        oneLL.setBackgroundColor(Color.RED);
        addView(oneLL);

        LinearLayout twoLL = new LinearLayout(getContext());
        twoLL.setBackgroundColor(Color.YELLOW);
        addView(twoLL);

        LinearLayout threeLL = new LinearLayout(getContext());
        threeLL.setBackgroundColor(Color.BLUE);
        addView(threeLL);

        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }
}
