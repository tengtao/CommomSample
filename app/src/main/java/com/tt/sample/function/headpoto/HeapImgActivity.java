package com.tt.sample.function.headpoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.tt.sample.R;
import com.tt.sample.base.BaseActivity;
import com.tt.sample.function.permission.PermissionActivity;
import com.tt.sample.ui.dialog.SelPhotoDialog;
import com.tt.sample.ui.view.ImageViewPlus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeapImgActivity extends BaseActivity {


    @BindView(R.id.head_img)
    ImageViewPlus headImg;
    @BindView(R.id.head_btn)
    Button headBtn;

    SelPhotoDialog selPhotoDialog;
    TakeHeadPoto takeHeadPoto;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_heapimg;
    }


    @Override
    public void inti() {
        super.inti();
        //
        takeHeadPoto = new TakeHeadPoto(this);
    }


    @OnClick({R.id.head_btn})
    void onAboutClick(View view) {
        switch (view.getId()) {
            case R.id.head_btn:
                showSelDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TakeHeadPoto.CAMERA:
                    //相机返回
                    takeHeadPoto.cameraCROP();
                    break;
                case TakeHeadPoto.PHOTO:
                    //从相册图片后返回的uri
                    takeHeadPoto.picCROP(data.getData());
                    break;
                case TakeHeadPoto.CROP:
                    Bitmap bitmap = takeHeadPoto.getImage();
                    headImg.setImageBitmap(bitmap);
                    //头像上传SelPhotoDialog
//                    accountModle.uploadImg(this, takeHeadPoto.getImgFile());
                    break;
            }
        }
    }

    /**
     * 请求相机权限，
     */
    private void requestPermission() {

        XXPermissions.with(this)
                //蓝牙定位权限
                .permission(Permission.CAMERA)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            //全部允许
                            takeHeadPoto.toCamer();
                        } else {
                            //还有部分权限没允许
                            showTipsDialog("请允许相机权限用于拍照");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(HeapImgActivity.this, permissions);
                        } else {
                            //提示
                            showTipsDialog("请允许相机权限用于拍照");
                        }
                    }
                });
    }

    void showSelDialog() {
        if (selPhotoDialog == null) {
            selPhotoDialog = new SelPhotoDialog(this, new SelPhotoDialog.SelItemCallBack() {
                @Override
                public void selCarmar() {
                    if (lacksPermission(Permission.CAMERA)) {
                        requestPermission();
                        return;
                    }
                    takeHeadPoto.toCamer();
                }

                @Override
                public void selPic() {
                    takeHeadPoto.toPhoto();
                }
            });
        }
        selPhotoDialog.show();
    }

}
