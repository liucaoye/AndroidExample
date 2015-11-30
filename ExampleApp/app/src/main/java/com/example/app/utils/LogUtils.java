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
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        String tag = stackTraceElement.getClassName();
        String method = stackTraceElement.getMethodName();
        Log.d(tag, "-----" + method + "-----");
    }

    private static String getClassTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        String tag = stackTraceElement.getClassName();
        return tag;
    }

    private static String getTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        String tag = stackTraceElement.getClassName() + "::"
                + stackTraceElement.getMethodName() + "::"
                + stackTraceElement.getLineNumber();
        return tag;
    }

    private static String getMethodTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[3];
        String tag = stackTraceElement.getClassName() + "::"
                + stackTraceElement.getMethodName();
        return tag;
    }

}
