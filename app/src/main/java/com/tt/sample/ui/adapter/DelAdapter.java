package com.tt.sample.ui.adapter;

import android.app.Activity;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tt.sample.R;

import java.util.List;

public class DelAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    Activity activity;

    public DelAdapter(Activity activity, List data) {
        super(R.layout.item_del_list, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_del_floor, item);
    }
}