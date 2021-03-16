package com.tt.sample.function.devinfo;


import android.content.Context;
import android.content.pm.PackageManager;

/**
 * 摄像头工具类
 */
public class CameraHelper {


    /**
     * FEATURE_CAMERA ： 后置相机
     * FEATURE_CAMERA_FRONT ： 前置相机
     */
    public static boolean isHasBackCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static boolean isHasFrontCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    /**
     * 获取摄像机支持参数
     */
}
