package com.tt.sample.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;

/**
 * Android10后台弹窗只显示蒙层，不显示布局
 * https://blog.csdn.net/qq_29418961/article/details/104583702
 * 为了解决Android 10的这个问题加的
 */
public class BaseDialog extends Dialog {


    public BaseDialog(@Nullable Context context) {
        super(context);
    }

    public BaseDialog(@Nullable Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@Nullable Context context, boolean cancelable,
                         @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Window window = getWindow();
        if (hasFocus && window != null) {
            View decorView = window.getDecorView();
            if (decorView.getHeight() == 0 || decorView.getWidth() == 0) {
                decorView.requestLayout();
            }
        }
    }
}
