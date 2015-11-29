package com.example.app.utils;

import android.util.Log;

/**
 * Author: LY
 * Date: 15/11/29
 */
public class LogUtils {


    public static void d(String msg) {
        Log.d(getTag(), msg);
    }

    public static void printMethod() {
        Log.d(getTag(), "------");
    }


    private static String getTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        String tag = stackTraceElement.getClassName() + "::"
                + stackTraceElement.getMethodName() + "::"
                + stackTraceElement.getLineNumber();
        return tag;
    }

}
