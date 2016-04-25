package com.example.myapplication.span;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.example.myapplication.R;

import org.w3c.dom.Text;

/**
 * 练习TextView的Span功能
 *
 *
 * 1. setIncludeFontPadding 起作用，但是不是完全好使
 *
 * */
public class SpanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_span);

        TextView textView = (TextView) findViewById(R.id.tv_span);
        changeTextSize(textView, "5200");
        textView.setIncludeFontPadding(false);
    }

    /**
     * @see AbsoluteSizeSpan
     * @see ImageSpan
     *
     * */
    private void changeTextSize(TextView textView, String str) {
        String text = String.format(getString(R.string.exp_money), str);
        SpannableString spannable = new SpannableString(text);
        int startIndex = text.indexOf(str);
        int endIndex = startIndex + str.length();
        spannable.setSpan(new AbsoluteSizeSpan(30), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        spannable.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
    }
}
