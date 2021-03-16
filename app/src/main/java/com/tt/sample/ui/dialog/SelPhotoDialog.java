package com.tt.sample.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tt.sample.R;


/**
 * 相册选择弹窗
 */
public class SelPhotoDialog extends Dialog implements View.OnClickListener {
    public SelItemCallBack selItemCallBack;

    private TextView carmar;
    private TextView photo;
    private TextView cancel;

    public SelPhotoDialog(Context context, SelItemCallBack event) {
        super(context, R.style.commonDialog);
        this.selItemCallBack = event;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sel_photo);
        initView();
    }

    private void initView() {
        carmar = (TextView) this.findViewById(R.id.dialog_selphoto_camere);
        carmar.setOnClickListener(this);
        photo = (TextView) this.findViewById(R.id.dialog_selphoto_photo);
        photo.setOnClickListener(this);
        cancel = (TextView) this.findViewById(R.id.dialog_selphoto_cancel);
        cancel.setOnClickListener(this);
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        if (attr != null) {
            attr.gravity = Gravity.BOTTOM;
            getWindow().setAttributes(attr);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_selphoto_camere:
                selItemCallBack.selCarmar();
                break;
            case R.id.dialog_selphoto_photo:
                selItemCallBack.selPic();
                break;
            case R.id.dialog_selphoto_cancel:
                break;
        }
        SelPhotoDialog.this.dismiss();
    }


    public interface SelItemCallBack {
        void selCarmar();

        void selPic();
    }


}
