package com.tt.sample.function.appinfo;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;
import com.tt.sample.MyApplication;
import com.tt.sample.R;
import com.tt.sample.function.jetpack.LiveDataCreateUtils;
import com.tt.sample.function.permission.PermissionActivity;
import com.tt.sample.utils.ToastUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;


/**
 * app更新下载
 * 不能持有Android的类，，
 */
public class AppUpdateModle extends ViewModel {

    private final static String APP_DOWNLOAD_VER = "appver";
    private final static String FileProvider = "com.tt.sample";

    //保存名字
    public static final String saveName = "xiaosuozhineng.apk";
    //保存位置, sd卡下面  Android/包名/file，不需要权限也可访问
    public static final String saveDir = MyApplication.getInstance().getExternalFilesDir("").getAbsolutePath();
    //安装路径
    public static final String apkPath = saveDir + File.separator + saveName;

    //app启动一次就更新一次，防止进入这个页面多次更新
    public static boolean needUpdate = true;
    //是否下载成功
    public static boolean isDownSuccess = true;


    //进条度的显示
    private MutableLiveData<String> loadingLiveData;
    //下载url
    private MutableLiveData<String> appUrlLiveData;
    //下载成功失败
    private MutableLiveData<Boolean> isDownLoadSuccessLiveData;

    public AppUpdateModle() {
        loadingLiveData = new MutableLiveData<>();
        appUrlLiveData = new MutableLiveData<>();
        isDownLoadSuccessLiveData = new MutableLiveData<>();
        LiveDataCreateUtils.createLiveData("aaa", String.class);
        LiveDataCreateUtils.getLiveData("aaa").postValue("dadada");
    }

    //返回LiveData类型
    public LiveData<String> getUserListLiveData() {
        return appUrlLiveData;
    }

    public LiveData<String> getLoadingLiveData() {
        return loadingLiveData;
    }

    public LiveData<Boolean> getIsDownLoadSuccessLiveData() {
        return isDownLoadSuccessLiveData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        appUrlLiveData = null;
        loadingLiveData = null;
        //UserRepository清除callback
    }

    /**
     * 检测更新请求
     */
    public void checkUpdate() {
        if (!needUpdate) {
            Logger.d("======= 已经校验过了不需要更新");
            return;
        }
        needUpdate = false;
        //1.检测app更新接口，，校验是否有新版本，
        //2.有版本更新显示弹窗，
        //3.点击确定开始下载
        //4.下载完成开始安装，


        //模拟是否下载
        boolean isupdate = true;
        String url = "https://imtt.dd.qq.com/16891/apk/FFB8E6609C4375C16430032F7C7DD330.apk?fsname=expression.app.ylongly7.com.expressionmaker_7.0.15_727.apk&csr=1bbd";
        if (isupdate) {
            appUrlLiveData.setValue(url);
        }
    }

    /**
     * 文件存在，，且下载成功，认为是下载过的，就不去从新下载
     */
    public boolean appIsDown() {
        File file = new File(apkPath);
        return file.exists() && isDownSuccess;
    }


    public void downloadApp(String url) {
        File file = new File(apkPath);
        if (file.exists()) {
            file.deleteOnExit();
        }

        Logger.d("=====saveDir " + saveDir);
        Logger.d("=====appUrl " + url);

        OkGo.<File>get(url)
                .tag("AppUpdateModle")
                .execute(new FileCallback(saveDir, saveName) {
                    @Override
                    public void downloadProgress(Progress progress) {
                        int progre = (int) (progress.currentSize * 100 / progress.totalSize);
                        Logger.d("=====downloadProgress" + progre);
                        loadingLiveData.postValue(progre + "");
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        Logger.d("=====onSuccess");
                        String[] strings = url.split("/");
                        String saveName = strings[strings.length - 1];
                        isDownLoadSuccessLiveData.postValue(true);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Logger.d("=====下载失败" + response.getException().getMessage());
                        isDownLoadSuccessLiveData.postValue(false);
                    }
                });
    }


}
