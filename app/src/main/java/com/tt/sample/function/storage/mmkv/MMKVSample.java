package com.tt.sample.function.storage.mmkv;


import com.orhanobut.logger.Logger;
import com.tencent.mmkv.MMKV;

/**
 *
 */
public class MMKVSample {


    /**
     *
     */
    public static void testMMKV() {
        //初始化
        MMKV.initialize("mmkv");
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


}
