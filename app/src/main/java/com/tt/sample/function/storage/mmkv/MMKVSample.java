package com.tt.sample.function.storage.mmkv;


import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;
import com.tt.sample.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class MMKVSample {


    /**
     *
     */
    public static void testMMKV() {
        MMKV kv = MMKV.defaultMMKV();
        Logger.d("====" + System.currentTimeMillis());
        for (int i = 0; i < 10000; i++) {
//            kv.encode("bool", true);
//            boolean bValue = kv.decodeBool("bool");
//            kv.encode("int", Integer.MIN_VALUE);
//            int iValue = kv.decodeInt("int");
            String key = i + "";
            String info = "info========" + i;
            kv.encode(key, info);

        }
        Logger.d("====" + System.currentTimeMillis());

//        for (int i = 0; i < 10000; i++) {
//            String key = i+"";
//            String str = kv.decodeString(key);
//            Logger.d("info====" +str);
//        }

        String[] key = kv.allKeys();
        Logger.d("key.length====" + key.length);
        for (int i = 0; i < key.length; i++) {
            String str = kv.decodeString(key[i]);
            Logger.d("key====" + key[i]);
            Logger.d("info====" + str);
        }
    }


    /**
     *
     */
    public static void testAddData() {
        MMKV kv = MMKV.defaultMMKV();
        kv.clearAll();
        for (int i = 0; i < 10000; i++) {
            String key = "No" + i;
            String info = "info========" + i;
            boolean result = kv.encode(key, info);
//            Logger.d(key + "====" + result);
        }
    }

    public static List<String> testGetAllData() {
        MMKV kv = MMKV.defaultMMKV();
        String[] key = kv.allKeys();
        if (key == null) {
            Logger.d("key.length====null");
            return null;
        }
        Logger.d("key.length====" + key.length);
        Logger.d("start====" + TimeUtils.getTimes(new Date()));
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < key.length; i++) {
            String str = kv.decodeString(key[i]);
//            Logger.d("key====" + key[i]);
//            Logger.d("info====" + str);
            stringList.add(str);
        }
        Logger.d("end====" + TimeUtils.getTimes(new Date()));
        return stringList;
    }

}
