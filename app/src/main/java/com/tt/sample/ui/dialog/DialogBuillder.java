package com.tt.sample.ui.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.tt.sample.R;


/**
 * 自定义弹窗，，
 */
public class DialogBuillder {

    BaseDialog dialog;
    View cancelView;
    View okView;
    EditText text;
    View.OnClickListener cancelListener;
    View.OnClickListener okListener;


    //如果必须有的属性加final
    private String title;

    //
    private String hit;
    //
    private int tipsColor = 0xff666666;
    private String tips;
    //
    private int cancelTxtColor = 0xff666666;
    private String cancelTxt = "取消";
    //
    private int okTxtColor = 0xff7270D3;
    private String okTxt = "确定";
    /**
     * 获取的编辑框数据
     */
    String edData;

    public DialogBuillder() {
    }

    public Dialog showLoadingDialog(Activity activity) {
        //如果之前的弹窗还有那么要他先消失
        dismiss();
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null);
        dialog = new BaseDialog(activity, R.style.loadDialog);
        //不可取消
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }


    /**
     * 编辑弹窗
     *
     * @param activity
     * @return
     */
    public Dialog showEdDialog(Activity activity) {
        //如果之前的弹窗还有那么要他先消失
        dismiss();
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_common_ed, null);
        TextView titleTv = layout.findViewById(R.id.eddialog_title);
        EditText ed = layout.findViewById(R.id.eddialog_ed);
        TextView cancel = layout.findViewById(R.id.eddialog_cancel);
        TextView ok = layout.findViewById(R.id.eddialog_ok);
        titleTv.setText(title);
        ed.setHint(hit);
        //
        cancel.setTextColor(cancelTxtColor);
        ok.setTextColor(okTxtColor);
        ok.setText(okTxt);
        cancelView = cancel;
        okView = ok;
        text = ed;
        if (cancelListener != null) {
            cancelView.setOnClickListener(cancelListener);
        } else {
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (okListener != null) {
            okView.setOnClickListener(okListener);
        }

        dialog = new BaseDialog(activity, R.style.commonDialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }


    /**
     * 修改模式弹窗
     */
    public Dialog showSwitchEdDialog(Activity activity) {
        //如果之前的弹窗还有那么要他先消失
        dismiss();
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_ed_switch_modle, null);
        TextView titleTv = layout.findViewById(R.id.eddialog_switch_title);
        TextView tipsTv = layout.findViewById(R.id.eddialog_switch_tips);
        EditText ed = layout.findViewById(R.id.eddialog_switch_ed);
        TextView cancel = layout.findViewById(R.id.eddialog_switch_cancel);
        TextView ok = layout.findViewById(R.id.eddialog_switch_ok);
        titleTv.setText(title);
        tipsTv.setText(tips);
        ed.setHint(hit);
        //
        cancel.setTextColor(cancelTxtColor);
        ok.setTextColor(okTxtColor);
        ok.setText(okTxt);
        cancelView = cancel;
        okView = ok;
        text = ed;
        if (cancelListener != null) {
            cancelView.setOnClickListener(cancelListener);
        } else {
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (okListener != null) {
            okView.setOnClickListener(okListener);
        }

        dialog = new BaseDialog(activity, R.style.commonDialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }

    /**
     * 操作选择弹窗
     */
    public Dialog buildSelDialog(Activity activity) {
        //如果之前的弹窗还有那么要他先消失
        dismiss();
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_select_tips, null);
        TextView titleTv = layout.findViewById(R.id.sel_dialog_title);
        TextView tipsTv = layout.findViewById(R.id.sel_dialog_tips);
        TextView cancelTv = layout.findViewById(R.id.sel_dialog_cancel);
        TextView okTv = layout.findViewById(R.id.sel_dialog_ok);
        cancelTv.setTextColor(cancelTxtColor);
        cancelTv.setText(cancelTxt);
        okTv.setTextColor(okTxtColor);
        okTv.setText(okTxt);
        titleTv.setText(title);
        tipsTv.setText(tips);
        cancelView = cancelTv;
        okView = okTv;
        if (cancelListener != null) {
            cancelView.setOnClickListener(cancelListener);
        } else {
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (okListener != null) {
            okView.setOnClickListener(okListener);
        }

        dialog = new BaseDialog(activity, R.style.commonDialog);
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }

    /**
     * 提示弹窗
     */
    public Dialog buildOnlyTipsDialog(Activity activity) {
        //如果之前的弹窗还有那么要他先消失
        dismiss();

        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_only_tips, null);
        TextView titleTv = layout.findViewById(R.id.dialog_only_title);
        TextView tipsTv = layout.findViewById(R.id.dialog_only_tips);
        TextView okTv = layout.findViewById(R.id.dialog_only_ok);
        titleTv.setText(title);
        tipsTv.setText(tips);
        okTv.setText(okTxt);
        okTv.setTextColor(okTxtColor);
        okView = okTv;
        if (okListener != null) {
            okView.setOnClickListener(okListener);
        }

        dialog = new BaseDialog(activity, R.style.commonDialog);
        //屏蔽返回键 传false
        dialog.setCancelable(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setContentView(layout);
        return dialog;
    }


    /**
     * =========================================================================
     */
    public String getEdStr() {
        if (text != null) {
            edData = text.getText().toString().trim();
        }
        return edData;
    }


    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public void onDestroy() {
        dismiss();
        cancelListener = null;
        okListener = null;
        dialog = null;
    }

    /**
     * =========================================================================
     */
    public DialogBuillder title(String title) {
        this.title = title;
        return this;
    }

    public DialogBuillder tips(String tips) {
        this.tips = tips;
        return this;
    }

    public DialogBuillder tips(String tips, int tipsColor) {
        this.tips = tips;
        this.tipsColor = tipsColor;
        return this;
    }

    public DialogBuillder hit(String hit) {
        this.hit = hit;
        return this;
    }

    public DialogBuillder cancelTxt(String cancelTxt) {
        this.cancelTxt = cancelTxt;
        this.cancelTxtColor = 0xff666666;
        return this;
    }

    public DialogBuillder cancelTxt(String cancelTxt, int cancelTxtColor) {
        this.cancelTxt = cancelTxt;
        this.cancelTxtColor = cancelTxtColor;
        return this;
    }

    public DialogBuillder okTxt(String okTxt) {
        this.okTxt = okTxt;
        this.okTxtColor = 0xff7270D3;
        return this;
    }

    public DialogBuillder okTxt(String okTxt, int okTxtColor) {
        this.okTxt = okTxt;
        this.okTxtColor = okTxtColor;
        return this;
    }


    public DialogBuillder setCancelListener(View.OnClickListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }


    public DialogBuillder setOkListener(View.OnClickListener okListener) {
        this.okListener = okListener;
        return this;
    }


}
