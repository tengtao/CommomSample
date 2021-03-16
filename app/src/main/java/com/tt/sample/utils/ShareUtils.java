package com.tt.sample.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.List;


public class ShareUtils {
    public static final String PACKAGE_WECHAT = "com.tencent.mm";
    public static final String PACKAGE_MOBILE_QQ = "com.tencent.mobileqq";
    public static final String PACKAGE_QZONE = "com.qzone";
    public static final String PACKAGE_SINA = "com.sina.weibo";


    /**
     * 调用微信api分享
     */
//    public static boolean shareWx(Activity activity, String description) {
//        if (!isInstallApp(activity, PACKAGE_WECHAT)) {
//            ToastUtil.showToast(activity, "请先安装微信");
//            return false;
//        }
//
//        //初始化一个WXTextObject对象，填写分享的文本对象
//        WXTextObject textObj = new WXTextObject();
//        textObj.text = description;
//        //初始化一个WXMediaMessage对象，填写标题、描述
//        WXMediaMessage msg = new WXMediaMessage(textObj);
//        msg.description = description;
//        //构造一个Req
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = String.valueOf(System.currentTimeMillis());
//        req.message = msg;
//        req.scene = SendMessageToWX.Req.WXSceneSession;
//        return MyApplication.api.sendReq(req);
//    }

    /**
     * 调用系统wx分享
     */
    public static void sendWx(Activity activity, String content) {
        if (isInstallApp(activity, PACKAGE_WECHAT)) {
            Intent intent = new Intent();
            ComponentName cop = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(cop);
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra("android.intent.extra.TEXT", content);
//            intent.putExtra("sms_body", content);
            intent.putExtra("Kdescription", !TextUtils.isEmpty(content) ? content : "");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } else {
            ToastUtil.showToast(activity, "请先安装微信");
        }
    }


    // 判断是否安装指定app
    public static boolean isInstallApp(Context context, String app_package) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String pn = pInfo.get(i).packageName;
                if (app_package.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 复制到剪切板
     */
    public static void copyTojqb(Activity activity, String msg) {
        ClipboardUtils.copyToClipboard(activity, msg);
        ToastUtil.showToast(activity, "密码已经复制到剪切板");
    }

    /**
     * 发送短信
     */
    public static void sendSendMsg(Activity activity, String msg) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", msg);
        activity.startActivity(intent);
    }


}
