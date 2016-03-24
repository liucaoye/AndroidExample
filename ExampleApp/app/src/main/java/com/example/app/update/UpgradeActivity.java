package com.example.app.update;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.app.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class UpgradeActivity extends AppCompatActivity {

    private static final int CLIENT_UPGRADE = 0;
    private static final int UMENG_UPGRADE = 1;
    private static final int CHOOSE_UPDATE = 0;
    private static final int FORCE_UPDATE = 1;


    /**
     * 升级方式：updateChannel(int)
     * 0:客户端升级
     * 1:友盟升级
     * */
    private int updateChannel;

    /**
     * 升级类型：updateType(int）
     * 0:选择更新
     * 1:强制更新
     * */
    private int updateType;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        updateChannel = CLIENT_UPGRADE;
        updateType = FORCE_UPDATE;


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 客户端更新
                if (updateChannel == CLIENT_UPGRADE) {

                    showDialog("客户端更新");
                    if (updateType == CHOOSE_UPDATE) {
                        // 选择更新

                    } else {
                        // 强制更新

                    }
                } else {
                    UmengUpdateAgent.setUpdateAutoPopup(false);
                    UmengUpdateAgent.setUpdateOnlyWifi(false);
                    UmengUpdateAgent.setUpdateListener(mUmengUpdateListener);
                    UmengUpdateAgent.update(UpgradeActivity.this);
                }
            }
        });
    }

    private UmengUpdateListener mUmengUpdateListener = new UmengUpdateListener() {
        @Override
        public void onUpdateReturned(int updateStatus, final UpdateResponse updateResponse) {
            switch (updateStatus) {
                case UpdateStatus.Yes:
//                    UmengUpdateAgent.showUpdateDialog(CheckUpdateActivity.this, updateResponse);
//                    boolean isIgnore =  UmengUpdateAgent.isIgnore(CheckUpdateActivity.this, updateResponse);
//                    File file = UmengUpdateAgent.downloadedFile(CheckUpdateActivity.this, updateResponse);
//                    Log.e(TAG, "检测是否为忽略版本: " + isIgnore + ", 检测是否已经下载完成: " + ((file == null) ? "未下载" : ""));

                    showDialog(updateResponse.updateLog);

                    break;
                case UpdateStatus.No:
                    Toast.makeText(UpgradeActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void showDialog(String updateInfo) {
        final UpdateInfoDialog dialog = new UpdateInfoDialog(UpgradeActivity.this);
        dialog.show();
        dialog.setUpdateInfo(updateInfo);
        final boolean isForce = updateType == CHOOSE_UPDATE ? false : true;
        dialog.isForceUpdate(isForce);
        dialog.setUpdateButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 强制更新
//                if (isForce) {
                    // 客户端
//                    Intent intent = new Intent(UpgradeActivity.this, UpdateActivity.class);
////                    intent.putExtra(UpdateActivity.UPDATE_INFO, updateResponse);
//                    intent.putExtra(UpdateActivity.FORCE_UPDATE, true);
//                    intent.putExtra(UpdateActivity.CLIENT_UPDATE, true);
//                    startActivity(intent);
                Intent serviceIntent = new Intent(UpgradeActivity.this, UpdateService.class);
                serviceIntent.putExtra("title", "正在下载");
                startService(serviceIntent);
//                } else {
//                    // 通知栏更新
//                }

            }
        });
        dialog.setLaterButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}
