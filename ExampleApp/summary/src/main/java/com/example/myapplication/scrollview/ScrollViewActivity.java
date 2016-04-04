package com.example.myapplication.scrollview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.myapplication.R;

public class ScrollViewActivity extends Activity {

    private ScrollView mScrollView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        mScrollView = (ScrollView) findViewById(R.id.scrollview);
        mTextView = (TextView) findViewById(R.id.tv_scroll_to);

        findViewById(R.id.clickBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("==TAG==", "滑动的距离是" + mTextView.getY());
                mScrollView.scrollTo(0, (int) mTextView.getY());
            }
        });
    }
}
