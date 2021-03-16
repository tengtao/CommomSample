package com.tt.sample.function.headpoto;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.orhanobut.logger.Logger;
import com.tt.sample.utils.DisplayUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TakeHeadPoto {
    Activity activity;

    public static final int CAMERA = 100;
    public static final int PHOTO = 101;
    public static final int CROP = 102;
    //
    public static final String provider = "com.tt.sample";
    //
    Uri mCutUri;

    public TakeHeadPoto(Activity activity) {
        this.activity = activity;
    }


    public void toCamer() {
        //创建一个file，用来存储拍照后的照片
        File outputfile = new File(activity.getExternalCacheDir(), "output.png");
        try {
            if (outputfile.exists()) {
                outputfile.delete();
            }
            outputfile.createNewFile();
        } catch (Exception e) {
            Logger.d("========" + e.getLocalizedMessage());
        }
        Uri imageuri;
        if (Build.VERSION.SDK_INT >= 24) {
            imageuri = FileProvider.getUriForFile(activity, provider, outputfile);
        } else {
            imageuri = Uri.fromFile(outputfile);
        }
        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        activity.startActivityForResult(intent, CAMERA);
    }

    /**
     * 打开相册获取图片
     */
    public void toPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, PHOTO);
    }


    /**
     * 图片裁剪
     */
    @NonNull
    public void picCROP(Uri uri) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            //设置裁剪之后的图片路径文件
            File cutfile = new File(activity.getExternalCacheDir(), "cutcamera.png");
            if (cutfile.exists()) { //如果已经存在，则先删除,这里应该是上传到服务器，然后再删除本地的，没服务器，只能这样了
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            Uri imageUri = uri; //返回来的 uri
            Uri outputUri = null; //真实的 uri
            outputUri = Uri.fromFile(cutfile);
            mCutUri = outputUri;
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置要裁剪的宽高，，这里影响上传头像的大小
            intent.putExtra("outputX", DisplayUtil.dip2px(activity, 100));
            intent.putExtra("outputY", DisplayUtil.dip2px(activity, 100));
            intent.putExtra("scale", true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data", false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            activity.startActivityForResult(intent, CROP);
        } catch (IOException e) {
            Logger.d("========" + e.getLocalizedMessage());
        }
    }

    /**
     * 拍照之后，启动裁剪
     */
    @NonNull
    public void cameraCROP() {
        try {
            String path = activity.getExternalCacheDir().getPath();
            String name = "output.png";
            //设置裁剪之后的图片路径文件
            File cutfile = new File(activity.getExternalCacheDir(), "cutcamera.png");
            if (cutfile.exists()) {
                cutfile.delete();
            }
            cutfile.createNewFile();
            //初始化 uri
            //返回来的 uri
            Uri imageUri = null;
            //真实的 uri
            Uri outputUri = null;
            Intent intent = new Intent("com.android.camera.action.CROP");
            //拍照留下的图片
            File camerafile = new File(path, name);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageUri = FileProvider.getUriForFile(activity,
                        provider,
                        camerafile);
            } else {
                imageUri = Uri.fromFile(camerafile);
            }
            outputUri = Uri.fromFile(cutfile);
            //把这个 uri 提供出去，就可以解析成 bitmap了
            mCutUri = outputUri;
            // crop为true是设置在开启的intent中设置显示的view可以剪裁
            intent.putExtra("crop", true);
            // aspectX,aspectY 是宽高的比例，这里设置正方形
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置要裁剪的宽高，这里影响上传头像的大小
            intent.putExtra("outputX", DisplayUtil.dip2px(activity, 100));
            intent.putExtra("outputY", DisplayUtil.dip2px(activity, 100));
            intent.putExtra("scale", true);
            //如果图片过大，会导致oom，这里设置为false
            intent.putExtra("return-data", false);
            if (imageUri != null) {
                intent.setDataAndType(imageUri, "image/*");
            }
            if (outputUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            }
            intent.putExtra("noFaceDetection", true);
            //压缩图片
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            activity.startActivityForResult(intent, CROP);
        } catch (IOException e) {
            Logger.d("========" + e.getLocalizedMessage());
        }
    }

    public Bitmap getImage() {
        Bitmap bitmap = null;
        try {
            //获取裁剪后的图片，并显示出来
            bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(mCutUri));

        } catch (FileNotFoundException e) {
            Logger.d("========" + e.getLocalizedMessage());
        }
        return bitmap;
    }


    /**
     * 获取要上传的文件
     */
    public File getImgFile() {
        return new File(activity.getExternalCacheDir(), "cutcamera.png");
    }
}
