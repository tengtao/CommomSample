package com.tt.sample.function.log;

import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.tt.sample.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 使用mmkv做log收集
 * 为了应对混淆找不到日志
 * =======================
 * 日记格式
 * 日期+操作+附加信息
 */
public class MyMMKVLog {
    //最多存1w条日志
    private static final int MAXSIZE = 2000;
    private static final String ID = "logid";
    private static final String cryptKey = "logkey";

    /**
     * @param action
     * @param log
     */
    public static void log(String action, String log) {
        MMKV kv = MMKV.mmkvWithID(ID, MMKV.SINGLE_PROCESS_MODE, cryptKey);
        //
        //时间
        String time = getCurrTimes();
        //
        String info = action + "==" + log + callMethodAndLine();
        String key = time + "==" + action;
        kv.encode(key, info);
        Logger.d("======" + info);
    }


    public static List<String> getLogList() {
        MMKV kv = MMKV.mmkvWithID(ID, MMKV.SINGLE_PROCESS_MODE, cryptKey);
        String[] key = kv.allKeys();
        if (key == null) {
            Logger.d("key.length====null");
            return null;
        }
        Logger.d("key.length====" + key.length);
        List<String> stringList = new ArrayList<>();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            String str = kv.decodeString(key[i]);
            stringList.add(str);
        }
        return stringList;
    }

    /**
     * 超过1w就删除掉前面5k，
     */
    public static void checkSize() {
        MMKV kv = MMKV.mmkvWithID(ID, MMKV.SINGLE_PROCESS_MODE, cryptKey);
        String[] key = kv.allKeys();
        if (key == null) {
            Logger.d("key.length====null");
            return;
        }
        if (key.length >= MAXSIZE) {
            //从小到大排列的，移除前面5k
            Arrays.sort(key);
            for (int i = 0; i < 1000; i++) {
                kv.remove(key[i]);
            }
        }

        Logger.d("检查后 key.length====" + kv.allKeys().length);
    }


    private static String callMethodAndLine() {
        String result = "";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")";
        result += ":-->" + thisMethodStack.getMethodName();
        return result;
    }


    private static String getCurrTimes() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

}
