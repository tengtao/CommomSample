package com.tt.sample.function.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {


    /**
     * 复制文件
     *
     * @param newPath
     * @param oldPath
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(String newPath, String oldPath) throws IOException {
        File newFile = new File(newPath);
        File oldFile = new File(oldPath);
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        inputChannel = new FileInputStream(newFile).getChannel();
        outputChannel = new FileOutputStream(oldFile).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());

    }





/**
 * 写入文件
 */
    //用于导出数据库文件，测试用
//    public static void copyFileToData() {
//        //找到文件的路径  /data/data/包名/databases/数据库名称
//        String dbname = Environment.getDataDirectory().getAbsolutePath()
//                + "/data/" + AppAplication.getInstance().getPackageName() + "/databases/learn.db";
//        File dbFile = new File(dbname);
//        FileInputStream fis = null;
//        FileOutputStream fos = null;
//        try {
//            //文件复制到sd卡中
//            fis = new FileInputStream(dbFile);
//            fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/learn.db");
//            int len = 0;
//            byte[] buffer = new byte[2048];
//            while (-1 != (len = fis.read(buffer))) {
//                fos.write(buffer, 0, len);
//            }
//            fos.flush();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//                if (fis != null) {
//                    fis.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
