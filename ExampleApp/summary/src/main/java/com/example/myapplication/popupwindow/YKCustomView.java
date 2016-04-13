package com.example.myapplication.popupwindow;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.myapplication.R;

/**
 * @author LIUYAN
 * @date 2016/3/30 0030
 * @time 16:12
 */
public class YKCustomView extends LinearLayout {

    public YKCustomView(Context context) {
        super(context, null);
    }

    public YKCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public YKCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.custom_view_layout, this);
    }
}
