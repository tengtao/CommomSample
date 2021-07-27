package com.tt.sample.function.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.orhanobut.logger.Logger;
import com.tt.sample.R;
import com.tt.sample.base.BaseActivity;
import com.tt.sample.function.log.MyMMKVLog;
import com.tt.sample.ui.adapter.DelAdapter;
import com.tt.sample.ui.dialog.DialogBuillder;
import com.tt.sample.ui.view.CustomLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/***
 * 选择器
 * 侧滑删除
 */
public class WebViewTestActivity extends BaseActivity {


    @BindView(R.id.wb_webview)
    BridgeWebView wbWebview;
    @BindView(R.id.wb_log)
    TextView wbLog;
    @BindView(R.id.wb_fun1)
    Button wbFun1;
    @BindView(R.id.wb_fun2)
    Button wbFun2;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_webview_java;
    }

    public void inti() {
        //获取webSettings
        WebSettings settings = wbWebview.getSettings();
        //让webView支持JS
        settings.setJavaScriptEnabled(true);
        wbWebview.setDefaultHandler(new DefaultHandler());
        wbWebview.setWebChromeClient(new WebChromeClient());
        //加载本地assets目录下的静态网页
        wbWebview.loadUrl("file:///android_asset/test_java.html");
        //
        //  注册监听方法当js中调用callHandler方法时会调用此方法（handlerName必须和js中相同）
        //js调用Java
        wbWebview.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                //显示js传递给Android的消息
                showToast("js返回:" + data);
                //Android返回给JS的消息
                function.onCallBack("我是js调用Android返回数据：");
            }
        });
        //
        //
        //java 调用js
        wbFun1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//              调用js中的方法（必须和js中的handlerName想同）
                wbWebview.callHandler("functionInJs", "Android调用js66", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        showToast(data);
                    }
                });
            }
        });

    }


    @OnClick({R.id.wb_fun1, R.id.wb_fun2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wb_fun1:
                break;
            case R.id.wb_fun2:
                break;
        }
    }


}