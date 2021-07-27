package com.tt.sample.function.handle;


import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class HandleUtils {

    //主线程handle
    private static Handler handler = new Handler(Looper.getMainLooper());
    //
    private static Map<String, Runnable> runnableMap = new HashMap<>();


    public static void postDelay(long delayTime, Runnable runnable) {
        handler.postDelayed(runnable, delayTime);
    }


    public static void postDelay(String tag, long delayTime, Runnable runnable) {
        runnableMap.remove(tag);
        runnableMap.put(tag, runnable);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delayTime);
    }

    public static void cancelDelay(String tag) {
        Runnable runnable = runnableMap.get(tag);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnableMap.remove(tag);
        }
    }

    public static void cancelAllDelay() {
        handler.removeCallbacksAndMessages(null);
    }

}
