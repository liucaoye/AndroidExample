package com.example.app.update;

import android.animation.FloatArrayEvaluator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.app.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateResponse;

import java.io.File;

/**
 * @author LIUYAN
 * @date 2016/3/21 0021
 * @time 18:16
 */
public class UpdateInfoDialog extends Dialog{

    private Button mUpdateButton;
    private Button mLaterRemindButton;
    private TextView mContentTextView;

    private boolean mForceUpdate;

    public UpdateInfoDialog(Context context) {
        super(context);
    }

    public UpdateInfoDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update_info);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        initView();
    }

    private void initView() {
        mUpdateButton = (Button) findViewById(R.id.btn_update);
        mLaterRemindButton = (Button) findViewById(R.id.btn_later_remind);
        mContentTextView = (TextView) findViewById(R.id.tv_content);
    }

    public void setUpdateInfo(String updateInfo) {
        mContentTextView.setText(updateInfo);
    }

    public void isForceUpdate(boolean bool) {
        if (bool) {
            mLaterRemindButton.setVisibility(View.GONE);
        } else {
            mLaterRemindButton.setVisibility(View.VISIBLE);
        }
    }

    public void setUpdateButtonClickListener(View.OnClickListener clickListener) {
        mUpdateButton.setOnClickListener(clickListener);
    }

    public void setLaterButtonClickListener(View.OnClickListener clickListener) {
        mLaterRemindButton.setOnClickListener(clickListener);
    }
}
