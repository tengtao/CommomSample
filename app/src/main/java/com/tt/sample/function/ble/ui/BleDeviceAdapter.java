package com.tt.sample.function.ble.ui;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import com.tt.sample.R;

public class BleDeviceAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    Activity activity;

    public BleDeviceAdapter(Activity activity, List data) {
        super(R.layout.item_bluetooth_device, data);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, BluetoothDevice item) {
        helper.setText(R.id.item_ble_name, item.getName())
                .setText(R.id.item_ble_mac, item.getAddress());
        TextView name = helper.getView(R.id.item_ble_name);
        helper.getView(R.id.item_ble_connectv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallback != null) {
                    clickCallback.onClick(item);
                }
            }
        });
    }

    public ClickCallback clickCallback;

    public void setClickCallback(ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    public interface ClickCallback {
        void onClick(BluetoothDevice item);
    }
}