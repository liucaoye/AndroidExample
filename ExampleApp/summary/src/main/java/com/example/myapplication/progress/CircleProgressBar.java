package com.example.myapplication.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication.R;


/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * @author xiaanming
 *
 */
public class CircleProgressBar extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;
	
	/**
	 * 圆环的颜色
	 */
	private int circleColor;
	
	/**
	 * 圆环进度的颜色
	 */
	private int circleProgressColor;
	
	/**
	 * 圆环的宽度
	 */
	private float circleWidth;
	
	/**
	 * 最大进度
	 */
	private int max;
	
	/**
	 * 当前进度
	 */
	private int progress;
	
	/**
	 * 进度的风格，实心或者空心
	 */
	private int style;
	
	public static final int STROKE = 0;
	public static final int FILL = 1;
	
	public CircleProgressBar(Context context) {
		this(context, null);
	}

	public CircleProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		paint = new Paint();

		
		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CircleProgressBar);
		
		//获取自定义属性和默认值
		circleColor = mTypedArray.getColor(R.styleable.CircleProgressBar_circleColor, Color.GRAY);
		circleProgressColor = mTypedArray.getColor(R.styleable.CircleProgressBar_circleProgressColor, Color.GREEN);
		circleWidth = mTypedArray.getDimension(R.styleable.CircleProgressBar_circleWidth, 5);
		max = mTypedArray.getInteger(R.styleable.CircleProgressBar_max, 100);
		style = mTypedArray.getInt(R.styleable.CircleProgressBar_style, 0);
		
		mTypedArray.recycle();
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/**
		 * 画最外层的大圆环
		 */
		int centre = getWidth()/2; //获取圆心的x坐标
		int radius = (int) (centre - circleWidth /2); //圆环的半径
		paint.setColor(circleColor); //设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); //设置空心
		paint.setStrokeWidth(circleWidth); //设置圆环的宽度
		paint.setAntiAlias(true);  //消除锯齿 
		canvas.drawCircle(centre, centre, radius, paint); //画出圆环
		
		/**
		 * 画圆弧 ，画圆环的进度
		 */
		
		//设置进度是实心还是空心
		paint.setStrokeWidth(circleWidth); //设置圆环的宽度
		paint.setColor(circleProgressColor);  //设置进度的颜色
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
		
		switch (style) {
		case STROKE:{
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 270, 360 * progress / max, false, paint);  //根据进度画圆弧
			break;
		}
		case FILL:{
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if(progress !=0)
				canvas.drawArc(oval, 270, 360 * progress / max, true, paint);  //根据进度画圆弧
			break;
		}
		}
		
	}
	
	
	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 获取进度.需要同步
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			postInvalidate();
		}
		
	}
	
	
	public int getCircleColor() {
		return circleColor;
	}

	public void setCircleColor(int circleColor) {
		this.circleColor = circleColor;
	}

	public int getCircleProgressColor() {
		return circleProgressColor;
	}

	public void setCircleProgressColor(int circleProgressColor) {
		this.circleProgressColor = circleProgressColor;
	}

	public float getCircleWidth() {
		return circleWidth;
	}

	public void setCircleWidth(float circleWidth) {
		this.circleWidth = circleWidth;
	}


}