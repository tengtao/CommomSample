package com.tt.sample.function.timer;


import android.os.Handler;

import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * handle实现超时的，
 */
public class HandleTimer {

    private static Handler handler = new Handler();
    private static HashMap<String, Runnable> runnableHashMap = new HashMap<>(5);

    public static void startTimeOut(String name, long timeOut, TimeoutCallback timeoutCallback) {
        stopTimer(name);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (timeoutCallback != null) {
                    timeoutCallback.onTimeOut();
                } else {
                    Logger.d("=====timeoutCallback ==null");
                }
            }
        };
        runnableHashMap.put(name, runnable);
        handler.postDelayed(runnable, timeOut);
    }

    public static void stopTimer(String name) {
        Runnable runnable = runnableHashMap.get(name);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnableHashMap.remove(name);
        } else {
            Logger.d("=====没这个任务:" + name);
        }
    }


    public interface TimeoutCallback {
        void onTimeOut();
    }
}
