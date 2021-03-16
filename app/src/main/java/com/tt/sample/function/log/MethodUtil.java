package com.tt.sample.function.log;

import android.util.Log;

/**
 * 打印时间
 */
public class MethodUtil {


    public static void printTime() {
        Log.d(callMethodAndLine(), System.currentTimeMillis() + "");
    }

    public static void d(String content) {
        Log.d(callMethodAndLine(), content);
    }

    public static void e(String content) {
        Log.e(callMethodAndLine(), content);
    }

    /**
     * Realization of double click jump events.
     *
     * @return 方法全部名，和方法
     * MyMQUtils(MyMQUtils.java:132):-->$1deliveryComplete
     */
    private static String callMethodAndLine() {
        String result = "";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")";
        result += ":-->>" + thisMethodStack.getMethodName();
        return result;
    }


}
