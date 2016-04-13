package com.example.myapplication;

import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class EditViewActivity extends Activity {

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);

        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.addTextChangedListener(mTextWatcher);
        mEditText.setFilters(new InputFilter[]{mHolderEtInputFilter});

    }

    private InputFilter mHolderEtInputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Log.e("==TAG==", "================================mHolderEtInputFilter====================================");
            Log.e("==TAG==", "====source: " + source + ", start: " + start + ", end: " + end);
            Log.e("==TAG==", "====dest: " + dest + ", dstart: " + dstart + ", dend: " + dend);

//            s = s.replaceAll(" ", "");
//            s = s.replaceAll("\\d","");
            return source;
        }
    };

    public String StringFilter(String str)throws PatternSyntaxException {
        String regEx = "\\s|[0-9]"; //要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            Log.e("==TAG==", "======beforeTextChanged====== s=" + s + ", start=" + start + ", after=" + after + ", count=" + count);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("==TAG==", "========onTextChanged======== s=" + s + ", start=" + start + ", before=" + before + ", count=" + count);

            String input = s.toString();
            String editable = mEditText.getText().toString();
            String str = StringFilter(editable.toString());
            if(!editable.equals(str)){
                mEditText.setText(str);
                mEditText.setSelection(str.length()); //光标置后
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.e("==TAG==", "======afterTextChanged======= s=" + s.toString());
        }
    };

    private void startCountTimer() {
        new CountDownTimer(300, 10) {
            int count = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("==TAG==", "===onTick===count=== " + count);
                count++;
            }

            @Override
            public void onFinish() {
                Log.e("==TAG==", "===onFinish===count=== " + count);
            }
        }.start();
    }

}
