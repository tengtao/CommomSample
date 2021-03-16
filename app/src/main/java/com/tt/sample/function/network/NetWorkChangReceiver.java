package com.tt.sample.function.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 监听网络状态变化
 * Created by Travis on 2017/10/11.
 */

public class NetWorkChangReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            //有网络
            if (networkInfo != null && networkInfo.isAvailable()) {

            }
            // 无网络
            else {
                if (netCallback != null) {
                    netCallback.onDisConnect();
                }
            }
        }
    }


    public interface NetCallback {
        void onDisConnect();
    }

    NetCallback netCallback;

    public void setNetCallback(NetCallback netCallback) {
        this.netCallback = netCallback;
    }
}