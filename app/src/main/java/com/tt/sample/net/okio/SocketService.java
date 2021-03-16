package com.tt.sample.net.okio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/**
 * 一般用到service 都是常驻后台的，
 * okio的简单例子
 */
public class SocketService extends Service {
    private static final String TAG = SocketService.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, "start onCreate~~~");
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "start onDestroy~~~");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
