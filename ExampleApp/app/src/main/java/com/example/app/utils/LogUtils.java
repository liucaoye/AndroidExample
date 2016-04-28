package com.example.app.utils;

import android.util.Log;

/**
 * Author: LY
 * Date: 15/11/29
 */
public class LogUtils {


    public static void d(String msg) {
        Log.println(Log.DEBUG, getTag(), msg);
    }

    public static void e(String msg) {
        Log.println(Log.ERROR, getTag(), msg);
    }

    public static void printMethod() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        String tag = stackTraceElement.getClassName();
        String method = stackTraceElement.getMethodName();
        Log.d(tag, "-----" + method + "-----");
    }

    private static String getTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String tag = stackTraceElement.getClassName() + "."
                + stackTraceElement.getMethodName() + "(Line:"
                + stackTraceElement.getLineNumber() + ")";
        return tag;
    }

}
