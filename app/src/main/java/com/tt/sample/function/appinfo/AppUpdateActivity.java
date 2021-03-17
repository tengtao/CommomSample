package com.tt.sample.function.appinfo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.content.FileProvider;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.orhanobut.logger.Logger;
import com.tt.sample.R;
import com.tt.sample.base.BaseActivity;
import com.tt.sample.function.jetpack.LiveDataCreateUtils;
import com.tt.sample.function.permission.PermissionActivity;

import java.io.File;
import java.util.List;

public class AppUpdateActivity extends BaseActivity {

    AppUpdateModle appUpdateModle;
    APPDownLoadUI appDownLoadUI;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_usb;
    }

    @Override
    public void inti() {
        super.inti();
        requestPermission();
        initViewModel();
        observeLivaData();

        appDownLoadUI = new APPDownLoadUI(this);
        appUpdateModle.checkUpdate();
    }

    private void initViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory());
        appUpdateModle = viewModelProvider.get(AppUpdateModle.class);
    }

    //观察ViewModel的数据，且此数据 是 View 直接需要的，不需要再做逻辑处理
    private void observeLivaData() {
        LiveDataCreateUtils.<String>getLiveData("aaa").observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Logger.d("=======" + s);
            }

        });


        appUpdateModle.getUserListLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String url) {
                //刷新列表
                appDownLoadUI.showUpdateDialog(AppUpdateActivity.this, "下载更新app吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        //本app下载
//                        if (appUpdateModle.appIsDown()) {
//                            installApk(AppUpdateActivity.this);
//                            return;
//                        }
//                        appUpdateModle.downloadApp(url);
                        //系统下载
                        requestPermission();
                    }
                });
            }
        });

        appUpdateModle.getLoadingLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String progress) {
                //显示/隐藏加载进度条
                appDownLoadUI.updateProgress("当前进度", Integer.parseInt(progress));
            }
        });

        appUpdateModle.getIsDownLoadSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                appDownLoadUI.dimissProgreDialog();
                if (isSuccess) {
                    installApk(AppUpdateActivity.this);
                } else {
                    showTipsDialog("下载错误");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (isSetInstallPermission && hasInstallPermission) {
                installApk(this);
                isSetInstallPermission = false;
            }
        }
    }


    /**
     * 允许安装未知应用返回
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d("=======onActivityResult: " + requestCode);
        //从允许安装未知应用界面返回
        if (requestCode == install) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    Logger.d("=======?????没安装权限 ");
                    isSetInstallPermission = true;
                    //提示需要获取安装权限,才能安装
                }
                //
                else {
                    //
//                    installApk(AppUpdateActivity.this);
                    //安装2
                    appDownLoadUI.installApk(AppUpdateActivity.this, 0);
                }
            }
        }
    }


    void requestPermission() {
        XXPermissions.with(this)
                //蓝牙定位权限
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            String url = "https://imtt.dd.qq.com/16891/apk/FFB8E6609C4375C16430032F7C7DD330.apk?fsname=expression.app.ylongly7.com.expressionmaker_7.0.15_727.apk&csr=1bbd";
                            //全部允许
                            appDownLoadUI.downLoadApkBySys(url, AppUpdateModle.saveName);
                        } else {
                            //还有部分权限没允许
                            showToast("请允许权限");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(AppUpdateActivity.this, permissions);
                        } else {
                            //提示
                            showToast("请允许权限");
                        }
                    }
                });

    }

    //
    public static boolean isSetInstallPermission = false;
    public static final int install = 50000;

    public void installApk(Activity activity) {
        //8.0以上要去开启安装应用权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //获取当前apk包URI，并设置到intent中（这一步设置，可让“未知应用权限设置界面”只显示当前应用的设置项）
                Uri packageURI = Uri.parse("package:" + activity.getPackageName());
                intent.setData(packageURI);
                activity.startActivityForResult(intent, install);
                return;
            }
        }

        File file = new File(AppUpdateModle.apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //authorities 对应  AndroidManifest.xml里面的authorities
            Uri apkUri = FileProvider.getUriForFile(activity, "com.tt.sample", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        //重置标志位
        activity.startActivity(intent);
    }

}
