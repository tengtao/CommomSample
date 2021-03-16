package com.tt.sample.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/**
 * 注册方法
 * </application>标签里面添加
 * <service android:name=".net.okio.SocketService" />
 */
public class SampleService extends Service {
    private static final String TAG = SampleService.class.getSimpleName();

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
