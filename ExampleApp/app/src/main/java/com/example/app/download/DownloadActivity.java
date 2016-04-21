package com.example.app.download;

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
import java.io.File;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String URL = "http://nj02.poms.baidupcs.com/file/8aa7d2aff35fa9338604a42cfb22af57?bkt=p3-14008aa7d2aff35fa9338604a42cfb22af571c80c29300000009f47d&fid=3725741735-250528-359539710826337&time=1461057585&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-k2rFfNEPxeDfFkocMgzwV9edLjo%3D&to=n2b&fm=Nan,B,U,nc&sta_dx=1&sta_cs=0&sta_ft=docx&sta_ct=5&fm2=Nanjing02,B,U,nc&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=14008aa7d2aff35fa9338604a42cfb22af571c80c29300000009f47d&sl=73269327&expires=8h&rt=pr&r=675933291&mlogid=2544764252320325611&vuk=3725741735&vbdid=2327769447&fin=%E7%AE%80%E7%90%86%E8%B4%A22.1-%E5%AE%9A%E6%9C%9F%281104%29.docx&fn=%E7%AE%80%E7%90%86%E8%B4%A22.1-%E5%AE%9A%E6%9C%9F%281104%29.docx&slt=pm&uta=0&rtype=1&iv=0&isw=0&dp-logid=2544764252320325611&dp-callid=0.1.1";

    private static final String FILE_NAME = "JLC.docx";

    private TextView mAppNameTv;
    private Button mDownloadBtn;
    private Button mPauseBtn;
    private ProgressBar mProgressBar;

    private Downloader mDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        mAppNameTv = (TextView) findViewById(R.id.tv_app_name);
        mDownloadBtn = (Button) findViewById(R.id.btn_download);
        mPauseBtn = (Button) findViewById(R.id.btn_pause);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_download);

        mDownloadBtn.setOnClickListener(this);
        mPauseBtn.setOnClickListener(this);

        mAppNameTv.setText(FILE_NAME);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Downloader.KMsgWhatCode) {
                int length = msg.arg1;
                mProgressBar.incrementProgressBy(length);
                if (mProgressBar.getProgress() == mProgressBar.getMax()) {
                    Toast.makeText(DownloadActivity.this, "下载完成！", Toast.LENGTH_SHORT).show();
                    mDownloader.delete(URL);
                    mDownloader.reset();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                String localFilePath = Environment.getDownloadCacheDirectory() + File.separator + FILE_NAME;
                int threadCount = 1;
                mDownloader = new Downloader(this, URL, localFilePath, threadCount, mHandler);
                if (mDownloader.isDownloading()) {
                    return;
                }
                LoadInfo loadInfo = mDownloader.getDownloaderInfos();
                mProgressBar.setMax(loadInfo.getFileSize());
                mProgressBar.setProgress(loadInfo.getCompleteSize());
                mDownloader.download();
                break;
            case R.id.btn_pause:
                if (mDownloader == null || !mDownloader.isDownloading()) {
                    return;
                }
                mDownloader.pause();
                break;
        }
    }
}
