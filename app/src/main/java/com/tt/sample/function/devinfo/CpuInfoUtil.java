package com.tt.sample.function.devinfo;

import android.util.Log;

import java.io.FileReader;
import java.io.IOException;


/**
 * 获取cpu信息，
 * 1.cpu架构：arm，armv7或者其他，
 * 通过读取/proc/cpuinfo得到cpu信息
 * 2.
 * 通过/proc/stat得到整个系统的 CPU 使用情况，
 * 3.
 * /proc/[pid]/stat可以得到某个进程的 CPU使用情况
 */
public class CpuInfoUtil {


    /**
     * 获取当前cpu类型，如ARMV7或者其他
     */
    public static String readCpuinfo() {
        String str = readFile("/proc/cpuinfo");
        String cpujg = "??";
        if (str != null && !str.equals("")) {
            if (str.contains("ARMv7")) {
                Log.d("readCpuinfo", "==================CPU TYPE : ARMV7 ");
                cpujg = "ARMv7";
            }
        }
        return cpujg;
    }


    /**
     * 获取cpu架构信息
     */
    private static String readFile(String path) {
        FileReader fr = null;
        StringBuilder sb = new StringBuilder();
        try {
            fr = new FileReader(path);
            int ch = 0;
            while ((ch = fr.read()) != -1) {
                sb.append((char) ch);
            }
            fr.close();
            Log.d("readFile ======[" + path + "]", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
