package com.example.myapplication;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class EditViewActivity extends Activity {

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);

        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("==TAG==", "s=" + s + ", start=" + start + ", before=" + before + ", count=" + count);
            String input = mEditText.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
