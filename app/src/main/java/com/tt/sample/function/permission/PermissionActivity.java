package com.tt.sample.function.permission;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.tt.sample.R;

import java.util.List;


/**
 * 文档地址
 * https://github.com/yanzhenjie/AndPermission
 * https://yanzhenjie.com/AndPermission/
 */
public class PermissionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

    }


    void requestPermission() {
        XXPermissions.with(this)
                //蓝牙定位权限
                .permission(Permission.ACCESS_FINE_LOCATION)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            //全部允许
                        } else {
                            //还有部分权限没允许
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(PermissionActivity.this, permissions);
                        } else {
                            //提示
                        }
                    }
                });
    }


}
