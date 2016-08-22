package com.example.app.lyrics.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;



/**
 * @author LIUYAN
 * @date 2016/1/18 0018
 * @time 18:50
 */
public class LyricView extends TextView {

    private Paint mCurrentPaint;
    private Paint mDefaultPaint;

    private float mWidth;
    private float mHeight;


    public LyricView(Context context) {
        super(context);
        init();
    }

    public LyricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {


        // 初始化paint
        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(Color.LTGRAY);

        mCurrentPaint = new Paint();
        mCurrentPaint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText("", 0, 0, mCurrentPaint);

    }







}
