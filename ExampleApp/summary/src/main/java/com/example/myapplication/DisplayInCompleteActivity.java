package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * 在有些手机上按钮显示不全
 *
 * */
public class DisplayInCompleteActivity extends Activity {

    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_in_complete);

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_show);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showDialog(DisplayInCompleteActivity.this);
            }
        });

    }

    public void showDialog(Context context) {
        Log.e("TAG", "----------showDialog----------");
        if (dialog == null) {
            dialog = new Dialog(context, R.style.Skipdialog);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.dialog_skip, null);
            dialog.setContentView(dialogView);
            dialog.setTitle("提示");
//            dialog.setCanceledOnTouchOutside(false);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.TOP | Gravity.RIGHT);
            // Android 4.4及以上dialog被状态栏遮挡，解决办法如下：
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            final Button mSkipBtn = (Button) dialogView.findViewById(R.id.btn_skip);
            mSkipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSkipBtn.setAlpha(0.7f);

                }
            });
//            Window dialogWindow = dialog.getWindow();
//            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
//            dialogWindow.setAttributes(lp);
        }

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            } else {
                dialog.show();
            }
        }

    }
}

