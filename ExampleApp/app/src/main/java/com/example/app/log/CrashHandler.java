package com.example.app.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LIUYAN
 * @date 2016/4/13 0013
 * @time 17:01
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private Map<String, String> infos = new HashMap<>();
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

    private static CrashHandler INSTANCE = new CrashHandler();

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理器 
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e("TAG", "error : " + e.getMessage());
            }

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

    }

    public boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常，正在收集日志，即将退出", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        collectDeviceInfo(mContext);
        saveCrashInfoToFile(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     * */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.e("TAG", field.getName() + ":" + field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String saveCrashInfoToFile(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);

        long timestamp = System.currentTimeMillis();
        String time = formatter.format(new Date());
        String fileName = "crash-" + time + "-" + timestamp + ".log";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/JLC";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(path + File.separator + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
                return fileName;
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
