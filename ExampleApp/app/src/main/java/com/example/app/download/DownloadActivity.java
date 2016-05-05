package com.example.app.download;

import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 断点下载
 * */
import com.example.app.R;

import java.io.File;

public class DownloadActivity extends Activity implements View.OnClickListener{

    private static final String URL = "http://images.mifengtd.cn/2011/07/GTD.jpg";

    private static final String FILE_NAME = "GTD.jpg";

    private TextView mAppNameTv;
    private Button mDownloadBtn;
    private Button mPauseBtn;
    private Button mResetBtn;
    private ProgressBar mProgressBar;

    private Downloader mDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        mAppNameTv = (TextView) findViewById(R.id.tv_app_name);
        mDownloadBtn = (Button) findViewById(R.id.btn_download);
        mPauseBtn = (Button) findViewById(R.id.btn_pause);
        mResetBtn = (Button) findViewById(R.id.btn_reset);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_download);

        mDownloadBtn.setOnClickListener(this);
        mPauseBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);

        mAppNameTv.setText(FILE_NAME);
        initDownload();
    }

    private void initDownload() {
        String localFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + FILE_NAME;
        int threadCount = 2;
        mDownloader = new Downloader(DownloadActivity.this, URL, localFilePath, threadCount, mHandler);
        if (mDownloader.isDownloading()) {
            return;
        }
        mDownloader.initDownloader();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Downloader.KMsgInitCode:
                    LoadInfo loadInfo = (LoadInfo) msg.obj;
                    mProgressBar.setMax(loadInfo.getFileSize());
                    mProgressBar.setProgress(loadInfo.getCompleteSize());
                    break;
                case Downloader.KMsgDownloadingCode:
                    int length = msg.arg1;
                    mProgressBar.incrementProgressBy(length);
                    if (mProgressBar.getProgress() == mProgressBar.getMax()) {
                        Toast.makeText(DownloadActivity.this, "下载完成！", Toast.LENGTH_SHORT).show();
                        mDownloader.complete();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                mDownloader.start();
                break;
            case R.id.btn_pause:
                if (mDownloader == null || !mDownloader.isDownloading()) {
                    return;
                }
                mDownloader.pause();
                break;
            case R.id.btn_reset:
                break;
        }
    }
}
