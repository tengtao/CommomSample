package com.tt.sample.function.devinfo;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.facebook.device.yearclass.DeviceInfo;
import com.facebook.device.yearclass.YearClass;
import com.tt.sample.R;

import static android.os.Debug.getRuntimeStat;

public class DevInfoActivity extends AppCompatActivity {
    TextView tv2, tv1, tv3, tv4;

    byte[] mBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devinfo);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);

        //
        setDevTips();
        setScreenTips();


        //测试内存占用，
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBytes = new byte[1024 * 1024 * 100];
                setDevTips();
                setScreenTips();
            }
        });
    }

    /**
     * RAM	   condition	Year Class
     * 768MB	1 core	    2009
     * ??       2+ cores	2010
     * 1GB	    <1.3GHz	    2011
     * ??        1.3GHz+	2012
     * 1.5GB	<1.8GHz  	2012
     * ??        1.8GHz+	2013
     * ??         2GB		2013
     * ??           3GB		2014
     * ??           5GB		2015
     * ??           more	2016
     */
    void setDevTips() {
        String phoneName = SystemUtil.getDeviceBrand() + " " + SystemUtil.getSystemModel();
        String devInfo = "手机名称：" + phoneName + "\n" + "当前安卓版本：" + SystemUtil.getSystemVersion() + "\n";

        String cpujg = CpuInfoUtil.readCpuinfo();
        devInfo += "\ncpu 类型" + cpujg;
        int khz = DeviceInfo.getCPUMaxFreqKHz();
        devInfo += "\ncpu 频率" + khz / 1000 / 1000.0 + "Ghz";
        long ramsize = DeviceInfo.getTotalMemory(getApplicationContext());
        devInfo += "运行内存" + ramsize / 1024 / 1024 + "M\n";


        int year = YearClass.get(getApplicationContext());
        if (year >= 2013) {
            // Do advanced animation
            devInfo += "中高端手机，可以添加复杂动画";
        } else if (year > 2010) {
            // Do simple animation
            devInfo += "手机比较低端，只可以添加简单动画，app体积不能太大";
        } else {
            // Phone too slow, don't do any animations
            devInfo += "最低端手机不可添加动画，";
        }
        //显示内存信息
        devInfo += displayBriefMemory();
        devInfo += getPss();


        //app
        long maxMemory = Runtime.getRuntime().maxMemory();
        devInfo += "\napp 最大可用内存" + maxMemory / 1024 / 1024.0 + "M\n";
        long totalMemory = Runtime.getRuntime().totalMemory();
        devInfo += "app 已获取内存" + totalMemory / 1024 / 1024.0 + "M\n";
        long freeMemory = Runtime.getRuntime().freeMemory();
        devInfo += "app 空闲可用内存" + freeMemory / 1024 / 1024.0 + "M\n";
        devInfo += getCDL();

        //
        tv1.setText(devInfo);
    }

    private String displayBriefMemory() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n系统剩余内存:" + (info.availMem / 1024 / 1024) + "m");
        stringBuilder.append("\n系统是否处于低内存运行：" + info.lowMemory);
        stringBuilder.append("\n当系统剩余内存低于" + (info.threshold / 1024 / 1024) + "m时就看成低内存运行");
        return stringBuilder.toString();
    }

    /***
     * https://blog.csdn.net/hudashi/article/details/7050897
     * https://blog.csdn.net/yangdeli888/article/details/43967595
     * PSS：实际使用的物理内存（比例分配共享库占用的内存）
     * 数据单位 kb
     */
    String getPss() {
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return "\n实际使用的物理内存(PSS) =" + memoryInfo.dalvikPss / 1024 + "mb\n";
    }

    /**
     * 触顶率
     * Java 内存使用超过最大限制的 85%,认为内存触顶
     */
    String getCDL() {
        long javaMax = Runtime.getRuntime().maxMemory();
        long javaTotal = Runtime.getRuntime().totalMemory();
        long javaUsed = javaTotal - Runtime.getRuntime().freeMemory();
        float proportion = (float) javaUsed * 100 / javaMax;
        return "app可用内存占用=" + String.format("%.2f", proportion) + "%";
    }


    void setScreenTips() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
        // 屏幕宽度（像素）
        int width = dm.widthPixels;
        // 屏幕高度（像素）
        int height = dm.heightPixels;
        // 屏幕密度（0.75 / 1.0 / 1.5）
        float density = dm.density;
        // 屏幕密度dpi（120 / 160 / 240）
        int densityDpi = dm.densityDpi;


        //分辨率
        String dipString = "手机屏幕宽高dip：" + dm.widthPixels / (dm.densityDpi / 160.0) +
                " * " + dm.heightPixels / (dm.densityDpi / 160.0);
        tv2.setText("手机分辨率(宽*高)：" + dm.widthPixels + " * " + dm.heightPixels + "\n" + dipString);
        //屏幕密度
        tv3.setText("屏幕密度(每英寸屏幕含有的像素个数)" + dm.densityDpi + "dp \n" + getdpi(dm.densityDpi));
        int dims = dm.widthPixels > dm.heightPixels ? dm.heightPixels : dm.widthPixels;
    }

    /**
     * 获取 dip提示
     *
     * @return
     */
    String getdpi(int dpi) {
        if (dpi > 120 && dpi <= 160) {
            return "手机会加载mdpi下面的图片,\n图标尺寸建议48*48 px";
        } else if (dpi > 160 && dpi <= 240) {
            return "手机会加载hdpi下面的图片,\n图标尺寸建议72*72 px";
        } else if (dpi > 240 && dpi <= 320) {
            return "手机会加载xdpi下面的图片,\n图标尺寸建议96*96 px";
        } else if (dpi > 320 && dpi <= 480) {
            return "手机会加载xxdpi下面的图片,\n图标尺寸建议144*144 px";
        } else if (dpi > 480 && dpi <= 640) {
            return "手机会加载xxxdpi下面的图片,\n图标尺寸建议192*192 px";
        } else {
            return "这个尺寸太奇怪";
        }
    }


    /**
     * gc次数,不是很有用
     */
    void getGCCount() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 运行的 GC 次数
            String gccount = getRuntimeStat("art.gc.gc-count");
            // GC 使用的总耗时，单位是毫秒
            String gctime = getRuntimeStat("art.gc.gc-time");
            // 阻塞式 GC 的次数
            String blockingcount = getRuntimeStat("art.gc.blocking-gc-count");
            // 阻塞式 GC 的总耗时
            String blockingtime = Debug.getRuntimeStat("art.gc.blocking-gc-time");
            Log.d("=========", "============gccount" + gccount + "\ngctime" + gctime
                    + "\nblockingcount" + blockingcount + "\nblockingtime" + blockingtime);
        } else {
            Log.d("=========", "getRuntimeStat not support");
        }
    }
}
