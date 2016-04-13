package com.example.myapplication.popupwindow;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.myapplication.R;

public class PopupWindowActivity extends Activity {

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(v);
                }
            }
        });


    }

    private void initPopupWindow() {
//        YKCustomView view = new YKCustomView(this);
//        TextView view = new TextView(this);
//        view.setText("TESTtestesttttestsete");
        View view = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);

        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(this.getResources().getDrawable(
                R.color.black));
        mPopupWindow.setOutsideTouchable(true);
    }
}
