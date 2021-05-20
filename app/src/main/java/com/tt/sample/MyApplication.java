package com.tt.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.tt.sample.function.storage.mmkv.MMKVSample;

import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class MyApplication extends Application {

    private static MyApplication instance;

    Handler handler;

    //单例模式中获取唯一的MyApplication实例
    public static MyApplication getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        intiLog();
        //主线程handle
        handler = new Handler(getMainLooper());
        //
        intiBle();

        registActivity();

        //初始化
        String rootDir = MMKV.initialize(this);
        MMKVSample.testAddData();
        Logger.d("=======MMKV.initialize  " + rootDir);
    }

    private void intiBle() {
//        if (BleManger.INATAN.isSupperBle(this)) {
//            BleManger.INATAN.init(this);
//            //添加特征值
//            BleCharacteristic characteristic = new BleCharacteristic();
//            characteristic.setServiceUUID(BleUUUID.serviceUUUID);
//            characteristic.setNotifyUUUID(BleUUUID.notifyUUUID);
//            characteristic.setWriteUUUID(BleUUUID.writeUUUID);
//            BleManger.INATAN.addBleCharacteristic(characteristic);
//            //设置后台扫描蓝牙
//            BleManger.INATAN.setScanBackstage(true);
//        }
    }


    /**
     * 日记
     */
    private void intiLog() {
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return true;
            }
        });
        //保存到磁盘
//        Logger.addLogAdapter(new DiskLogAdapter());
    }


    //=============================================================================
    private List<Activity> activityList = new LinkedList<>();

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    //移除
    public void removeActivity(Activity activity) {
        activity.finish();
        activityList.remove(activity);
    }

    //移除特定名字的activity
    public void removeActivity(String activityName) {
        for (int i = 0; i < activityList.size(); i++) {
            if (activityName.equals(activityList.get(i).getClass().getSimpleName())) {
                activityList.get(i).finish();
                activityList.remove(activityList.get(i));
                break;
            }
        }
    }

    //遍历所有Activity并finish
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }


    void registActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }
    //=============================================================================
}
