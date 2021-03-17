package com.tt.sample.function.devinfo;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 控制安卓设备的开关
 */
public class DevControlUtils {

    /**
     * https://blog.csdn.net/weixin_45050400/article/details/91826305
     * 设置亮度需要获取这两个权限
     * <uses-permission android:name="android.permission.CHANGE_CONFIGURATION"/>
     * <uses-permission android:name="android.permission.WRITE_SETTINGS"/>
     */

    /**
     * 获取当前屏幕亮度
     *
     * @param context
     * @return
     */
    public static int getCurrentScreenBrightness(Context context) {
        int currentBrightness = 0;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            currentBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return currentBrightness;
    }


    /**
     * 获取当前系统亮度模式
     * 返回值是0（手动调节模式）或者1（自动调节模式）；
     *
     * @param context
     * @return
     */
    public static int getCurrentScreenBrightnessMode(Context context) {
        int screenBrightnessMode = 0;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            screenBrightnessMode = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return screenBrightnessMode;
    }


    /**
     * 0-255之间
     *
     * @param context
     */
    public static void setSystemLight(Context context, int light) {
        //还是需要在6.0以上的手机上申请android.permission.WRITE_SETTINGS权限；
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                setSystemBrightness(context, light);
            }
        }
    }

    /**
     * @param context
     * @param brightness
     */
    private static void setSystemBrightness(Context context, int brightness) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        contentResolver.notifyChange(uri, null);
    }

    /**
     * 设置当前activity屏幕亮度，不影响系统亮度
     * 需要申请权限android.permission.CHANGE_CONFIGURATION
     * <uses-permission android:name="android.permission.CHANGE_CONFIGURATION"/>
     * 这里的是百分比亮度
     */
    public static void setCurrentBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.screenBrightness = brightness * (1f / 255f);
        activity.getWindow().setAttributes(layoutParams);
    }


    //============================================================

    public static void setVoi(Context context, int vol) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        //通话音量
        int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
        int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);

        //系统音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);

        //铃声音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);


        //提示声音音量
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);

        //音乐音量，一般用的这个
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Logger.d("音乐最大音量===" + max + "     当前音量===" + current);

        //设置音量大小，第一个参数：STREAM_VOICE_CALL（通话）、STREAM_SYSTEM（系统声音）、STREAM_RING（铃声）
        // 、STREAM_MUSIC（音乐）和STREAM_ALARM（闹铃）；
        // 第二个参数：音量值，取值范围为0-7；
        // 第三个参数：可选标志位，用于显示出音量调节UI（AudioManager.FLAG_SHOW_UI）。
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, AudioManager.FLAG_PLAY_SOUND);
    }

    //============================================================


    /**
     * 创建热点
     * 8.0不行
     *
     * @param mSSID   热点名称
     * @param mPasswd 热点密码
     * @param isOpen  是否是开放热点
     */
    public static void startWifiAp(Context context, String mSSID, String mPasswd, boolean isOpen) {
        Method method1 = null;
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        try {
            method1 = mWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            WifiConfiguration netConfig = new WifiConfiguration();

            netConfig.SSID = mSSID;
            netConfig.preSharedKey = mPasswd;
            netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            if (isOpen) {
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            } else {
                netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            }
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            netConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            netConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            method1.invoke(mWifiManager, netConfig, true);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 不行，，8.0提示出错
     *
     * @param context
     */
    public static void startTethering(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            int wifiState = mWifiManager.getWifiState();
            boolean isWifiEnabled = ((wifiState == WifiManager.WIFI_STATE_ENABLED) || (wifiState == WifiManager.WIFI_STATE_ENABLING));
            if (isWifiEnabled)
                mWifiManager.setWifiEnabled(false);
        }
        if (mConnectivityManager != null) {
            try {
                Field internalConnectivityManagerField = ConnectivityManager.class.getDeclaredField("mService");
                internalConnectivityManagerField.setAccessible(true);
                WifiConfiguration apConfig = new WifiConfiguration();
                apConfig.SSID = "cuieney";
                apConfig.preSharedKey = "12121212";

                StringBuffer sb = new StringBuffer();
                Class internalConnectivityManagerClass = Class.forName("android.net.IConnectivityManager");
                ResultReceiver dummyResultReceiver = new ResultReceiver(null);
                try {

                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    Method mMethod = wifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
                    mMethod.invoke(wifiManager, apConfig);
                    Method startTetheringMethod = internalConnectivityManagerClass.getDeclaredMethod("startTethering",
                            int.class,
                            ResultReceiver.class,
                            boolean.class);

                    startTetheringMethod.invoke(internalConnectivityManagerClass,
                            0,
                            dummyResultReceiver,
                            true);
                } catch (NoSuchMethodException e) {
                    @SuppressLint("SoonBlockedPrivateApi") Method startTetheringMethod = internalConnectivityManagerClass.getDeclaredMethod("startTethering",
                            int.class,
                            ResultReceiver.class,
                            boolean.class,
                            String.class);

                    startTetheringMethod.invoke(internalConnectivityManagerClass,
                            0,
                            dummyResultReceiver,
                            false,
                            context.getPackageName());
                } catch (InvocationTargetException e) {
                    sb.append(11 + (e.getMessage()));
                    e.printStackTrace();
                } finally {
                    Logger.d(sb.toString() + "============");
                }


            } catch (Exception e) {
                Logger.d(e.getMessage() + "============");
            }
        }
    }


    /**
     * 获取热点名
     **/
    public static String getApSSID(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method localMethod = mWifiManager.getClass().getDeclaredMethod("getWifiApConfiguration", new Class[0]);
            if (localMethod == null) return null;
            Object localObject1 = localMethod.invoke(mWifiManager, new Object[0]);
            if (localObject1 == null) return null;
            WifiConfiguration localWifiConfiguration = (WifiConfiguration) localObject1;
            if (localWifiConfiguration.SSID != null) return localWifiConfiguration.SSID;
            Field localField1 = WifiConfiguration.class.getDeclaredField("mWifiApProfile");
            if (localField1 == null) return null;
            localField1.setAccessible(true);
            Object localObject2 = localField1.get(localWifiConfiguration);
            localField1.setAccessible(false);
            if (localObject2 == null) return null;
            Field localField2 = localObject2.getClass().getDeclaredField("SSID");
            localField2.setAccessible(true);
            Object localObject3 = localField2.get(localObject2);
            if (localObject3 == null) return null;
            localField2.setAccessible(false);
            String str = (String) localObject3;
            return str;
        } catch (Exception localException) {
        }
        return null;
    }


    /**
     * 检查是否开启Wifi热点
     *
     * @return
     */
    public static boolean isWifiApEnabled(Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (boolean) method.invoke(mWifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭热点
     */
    public static void closeWifiAp(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (isWifiApEnabled(context)) {
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method2 = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method2.invoke(wifiManager, config, false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开热点手机获得其他连接手机IP的方法
     *
     * @return 其他手机IP 数组列表
     */
    public static ArrayList<String> getConnectedIP() {
        ArrayList<String> connectedIp = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    if (!ip.equalsIgnoreCase("ip")) {
                        connectedIp.add(ip);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return connectedIp;
    }

}

