package com.tt.sample.function.bugly;


import android.content.Context;

import com.tencent.bugly.crashreport.CrashReport;
import com.tt.sample.MyApplication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * 收集一些常见错误上传bugly
 */
public class BuglyUtils {


    public static void uploaLog(String errorMsg) {
        CrashReport.postCatchedException(new Exception(errorMsg));
    }

    /**
     * @param errorMsg 错误
     * @param action   执行的逻辑
     * @param info     信息
     *                 putUserData ，上传参数，
     *                 自定义Map参数可以保存发生Crash时的一些自定义的环境信息。在发生Crash时会随着异常信息一起上报并在页面展示。
     *                 CrashReport.putUserData(context, “userkey”, “uservalue”);
     *                 注意：最多可以有9对自定义的key-value（超过则添加失败），key限长50字节、value限长200字节，过长截断。
     *                 <p>
     *                 cao:之前设置的也会上报，所以要把这个删除
     */
    public static void uploaLog(String errorMsg, String action, String info) {
        Context context = MyApplication.getInstance();

        try {
            //把之前的先删除
            //注意这里要用迭代器，否则 ConcurrentModificationException
            Set<String> strings = CrashReport.getAllUserDataKeys(context);
            Iterator<String> iterator = strings.iterator();
            while (iterator.hasNext()) {
                iterator.remove();
            }
            //传递环境参数
            CrashReport.putUserData(context, action, info);
            throw new Exception(errorMsg);
        } catch (Throwable thr) {
            // bugly会将这个throwable上报
            CrashReport.postCatchedException(thr);
        }
    }


    public static void uploaLog(String errorMsg, HashMap<String, String> errorMap) {
        Context context = MyApplication.getInstance();
        //把之前的先删除
        //注意这里要用迭代器，否则ConcurrentModificationException
        Set<String> strings = CrashReport.getAllUserDataKeys(context);
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
        if (errorMap != null) {
            for (String key : errorMap.keySet()) {
                //传递环境参数
                CrashReport.putUserData(context, key, errorMap.get(key));
            }
        }
        //上传日志
        CrashReport.postCatchedException(new Exception(errorMsg));
    }


    /**
     *
     */
    public static void testCrash() {
        CrashReport.testJavaCrash();
    }
}
