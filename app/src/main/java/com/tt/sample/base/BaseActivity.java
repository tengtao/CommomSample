package com.tt.sample.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.tt.sample.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础activity
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mUnbinder;
    //
    private AlertDialog.Builder mBuilder;
    //
    private ProgressDialog progressDialog;
    //是不是在前台
    boolean isForeground = false;

    protected BaseActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
//        Objects.requireNonNull(getSupportActionBar()).hide();
        setTitle("入库管理");
        mUnbinder = ButterKnife.bind(this);
        inti();
    }


    public void inti() {
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * 判断是否有权限，返回true就是没有权限
     */
    protected boolean lacksPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED;
    }


    /**
     * 显示提示dialog
     */
    protected void showTipsDialog(String text) {
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(this);
            mBuilder.setCancelable(true);
            mBuilder.setTitle("提示");
            mBuilder.setMessage(text);
            mBuilder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        mBuilder.setMessage(text);
        mBuilder.create().show();
    }

    protected void showTipsDialog(String text, DialogInterface.OnClickListener onClickListener) {
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(this);
            mBuilder.setCancelable(true);
            mBuilder.setTitle("提示");
            mBuilder.setMessage(text);
            mBuilder.setPositiveButton("好的", onClickListener);
        }
        mBuilder.setCancelable(false);
        mBuilder.setMessage(text);
        mBuilder.create().show();
    }


    /**
     * 普通加载弹窗
     */
    protected void showLoadingDialog(String text) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(text);
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    protected void stopLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showToast(String message) {
        ToastUtil.showToast(this, message);
    }

    /**
     * 打开activity
     */
    public void openActivity(Class<?> cls) {
        stopLoading();
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void openActivity(Intent intent) {
        stopLoading();
        startActivity(intent);
    }

    public void finishWithAnim() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public abstract int getLayoutResID();


}
