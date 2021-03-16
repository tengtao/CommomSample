package com.tt.sample.function.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.orhanobut.logger.Logger;
import com.tt.sample.R;
import com.tt.sample.function.permission.PermissionActivity;

import java.io.IOException;
import java.util.List;


/***
 * 摄像头预览，
 */
public class CameraDemoActivity extends AppCompatActivity {

    TextView infoTv;
    SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    SurfaceHolder.Callback callback;
    //
    private static int mOrientation = 0;
    //后置摄像头
    private static int mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;

    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        infoTv = findViewById(R.id.camerainfotv);
        mSurfaceView = findViewById(R.id.camerasuview);
        requestPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy=============");
        releaseCamera();

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
                            //接受
                            initView();
                            initCamera();
                        } else {
                            //还有部分权限没允许
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(CameraDemoActivity.this, permissions);
                        } else {
                            //提示
                            Logger.d("======permissions onDenied=======");
                        }
                    }
                });
    }

    public void initView() {
        mSurfaceHolder = mSurfaceView.getHolder();
        callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Logger.d("======surfaceCreated=======");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //当SurfaceView变化时也需要做相应操作，这里未做相应操作
                Logger.d("======surfaceChanged=======");
                initCamera();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Logger.d("======surfaceDestroyed=======");
                mCamera.stopPreview();
            }
        };
        mSurfaceHolder.addCallback(callback);
    }

    private void initCamera() {
        if (mCamera != null) {
            releaseCamera();
            System.out.println("===================releaseCamera=============");
        }
        mCamera = Camera.open(mCameraID);
        System.out.println("===================openCamera=============");
        if (mCamera == null) {

            return;
        }
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置参数
        setParameters(mCamera);

        //设置旋转角度，否则会出现问题
        mOrientation = calculateCameraPreviewOrientation(this);
        mCamera.setDisplayOrientation(mOrientation);
        //通过setPreviewCallback方法监听预览的回调：
        mCamera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] bytes, Camera camera) {
                //这里面的Bytes的数据就是NV21格式的数据,或者YUV_420_888的数据


            }
        });
        mCamera.startPreview();
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mSurfaceHolder.removeCallback(callback);
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;

        }
    }

    /**
     * 设置预览参数
     *
     * @param mCamera
     */
    void setParameters(Camera mCamera) {
        Camera.Parameters parameters = mCamera.getParameters();
        //
        parameters.setRecordingHint(true);
        //设置返回的数据格式，下面的setPreviewCallback用到
        parameters.setPreviewFormat(ImageFormat.NV21);
        //这个用不了，会报错
//        parameters.setPreviewFormat(ImageFormat.YUV_420_888);
        //
        if (mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        //打印一下支持的预览格式
        List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
        for (int i = 0; i < sizeList.size(); i++) {
            Logger.d("支持的预览尺寸：");
            Logger.d(sizeList.get(i).width + "," + sizeList.get(i).height);
            infoTv.append(sizeList.get(i).width + "," + sizeList.get(i).height + "\n");
        }
        //设置预览尺寸,需要和支持的预览尺寸一致，否则会显示不出来
        //可以写一个函数选择预览尺寸
        int index = getPreviewSize(sizeList);
        parameters.setPreviewSize(sizeList.get(index).width, sizeList.get(index).height);
        mCamera.setParameters(parameters);
    }


    /**
     * 屏幕的宽度==预览高度那是最合适
     *
     * @param sizeList
     */
    int getPreviewSize(List<Camera.Size> sizeList) {

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int index = 0;
        //最接近屏幕的
        for (int i = 0; i < sizeList.size(); i++) {
            if (width == sizeList.get(i).height) {
                index = i;
                break;
            }
        }
        return index;
    }


    /**
     * 设置预览角度，setDisplayOrientation本身只能改变预览的角度
     * previewFrameCallback以及拍摄出来的照片是不会发生改变的，拍摄出来的照片角度依旧不正常的
     * 拍摄的照片需要自行处理
     */
    public int calculateCameraPreviewOrientation(Activity activity) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        System.out.println("=======info.orientation===========" + info.orientation + degrees);
        return result;
    }


}
