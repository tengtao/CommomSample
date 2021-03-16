package com.tt.sample.function.appinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;

import androidx.core.content.FileProvider;

import com.orhanobut.logger.Logger;
import com.tt.sample.MyApplication;

import java.io.File;


public class APPDownLoadUI {
    //
    public static boolean isSetInstallPermission = false;
    public static final int install = 50000;


    public static final String APK_DOWNLOAD_FILE = MyApplication.getInstance().getExternalFilesDir("").getAbsolutePath()
            + File.separator
            + Environment.DIRECTORY_DOWNLOADS
            + File.separator + "xiaosuozhineng.apk";
    private Activity mActivity;

    long downID = 0;

    public APPDownLoadUI(Activity activity) {
        this.mActivity = activity;
        initDialog(activity);
        setReceiver(activity);
    }

    private boolean isRegisterReceiver;

    private void setReceiver(Activity activity) {
        if (!isRegisterReceiver) {
            DownloadReceiver receiver = new DownloadReceiver(this);
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            activity.registerReceiver(receiver, intentFilter);
            isRegisterReceiver = true;
        }
    }


    private void toSet() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //获取当前apk包URI，并设置到intent中（这一步设置，可让“未知应用权限设置界面”只显示当前应用的设置项）
        Uri packageURI = Uri.parse("package:" + mActivity.getPackageName());
        intent.setData(packageURI);
        mActivity.startActivityForResult(intent, install);
    }

    //提示弹窗
    private AlertDialog.Builder mBuilder;

    public void showUpdateDialog(Activity activity, String msg, DialogInterface.OnClickListener clickListener) {
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(activity);
            mBuilder.setCancelable(true);
            mBuilder.setTitle("提示");
            mBuilder.setMessage(msg);
            mBuilder.setPositiveButton("好的", clickListener);
            mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        mBuilder.setMessage(msg);
        mBuilder.create().show();
    }

    /**
     * 提示没有安装权限，
     */
    private Dialog tipsDialog;

    public void showAppInstallTipsDialog(Activity activity) {
        if (tipsDialog == null) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
            mBuilder.setCancelable(true);
            mBuilder.setTitle("提示");
            mBuilder.setMessage("请打开app安装权限");
            mBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //去开启权限
                }
            });
            mBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            tipsDialog = mBuilder.create();
        }
        tipsDialog.show();
    }


    public void dimissTipsDialog() {
        if (tipsDialog != null && tipsDialog.isShowing()) {
            tipsDialog.dismiss();
        }
    }

    public static class DownloadReceiver extends BroadcastReceiver {
        APPDownLoadUI appDownLoadUI;

        DownloadReceiver(APPDownLoadUI appDownLoadUI) {
            this.appDownLoadUI = appDownLoadUI;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                appDownLoadUI.installApk(context, id);
            } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                //处理 如果还未完成下载，用户点击Notification ，跳转到下载中心
                Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
                viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(viewDownloadIntent);
            }
        }


    }

    /**
     * 启动安装
     *
     * @param context
     * @param downloadApkId 不知道为啥安装不了，，
     */
    public void installApk(Context context, long downloadApkId) {
        if (downloadApkId != 0) {
            downID = downloadApkId;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                toSet();
                return;
            }
        }

        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadFileUri = dManager.getUriForDownloadedFile(downID);


        Logger.d("=====" + APK_DOWNLOAD_FILE);
        if (downloadFileUri != null) {
            File file = new File(APPDownLoadUI.APK_DOWNLOAD_FILE);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //判读版本是否在7.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //authorities 对应  AndroidManifest.xml里面的authorities
                Uri apkUri = FileProvider.getUriForFile(context, "com.tt.sample", file);
                //Granting Temporary Permissions to a URI
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
        } else {
            Log.e("======DownloadManager", "download error");
        }
    }

    /**
     *
     */
    public static void downLoadApkBySys(String apkUri, String downloadName) {

        Context context = MyApplication.getInstance();
        //创建request对象
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUri));
        //设置什么网络情况下可以下载
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //制定下载的文件类型为APK
        request.setMimeType("application/vnd.android.package-archive");
        // 下载过程和下载完成后通知栏有通知消息。
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE
                | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //设置通知栏的标题
        request.setTitle("应用名称");
        //设置通知栏的message
        request.setDescription("正在下载应用名称...");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        //设置漫游状态下是否可以下载
        request.setAllowedOverRoaming(false);
        //表示允许MediaScanner扫描到这个文件，默认不允许。
        request.allowScanningByMediaScanner();
        //设置文件存放目录
        //这个不是在
        //创建目录，sd卡download文件夹
//        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadName);
        //app文件夹内
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, downloadName);

        //获取系统服务
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //        startActivity(new android.content.Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));//启动系统下载界面
        //进行下载
        downloadManager.enqueue(request);
    }


    //下载apk进度条
    private ProgressDialog dialog;

    private void initDialog(Activity activity) {
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
        //
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    public void updateProgress(String msg, int pro) {
        if (dialog == null) {
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        dialog.setMessage(msg);
        dialog.setProgress(pro);
    }

    public void dimissProgreDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
