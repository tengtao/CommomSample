package com.tt.sample.function.ble.ui;

import android.app.Activity;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tt.sample.R;

import java.util.List;

public class BleBackMsgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    Activity activity;

    public BleBackMsgAdapter(Activity activity, List data) {
        super(R.layout.item_log, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_log_tv, item);
    }
}