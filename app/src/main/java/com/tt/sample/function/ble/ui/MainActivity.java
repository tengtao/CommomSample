package com.tt.sample.function.ble.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.Button;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.orhanobut.logger.Logger;
import com.tt.sample.R;
import com.tt.sample.function.ble.BleManger;
import com.tt.sample.function.ble.ScanRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseBleActivity {

    @BindView(R.id.main_rv)
    RecyclerView mainRv;
    @BindView(R.id.main_refresh)
    SwipeRefreshLayout mainRefresh;
    @BindView(R.id.main_btn)
    Button mainBtn;

    List<BluetoothDevice> deviceList = new ArrayList<>();

    BleDeviceAdapter bleDeviceAdapter;

    //
    HashMap<String, String> stringHashMap = new HashMap<>();
    HashMap<String, String> hasCommit = new HashMap<>();

    @Override
    public int getLayoutResID() {
        return R.layout.activity_ble_main;
    }

    @Override
    public void inti() {
        super.inti();
        setRv();
        requestPermission();
    }

    @OnClick(R.id.main_btn)
    public void onViewClicked() {
        //提交后台成功，删除当前
        //清除已经提交的
        for (int i = 0; i < deviceList.size(); i++) {
            hasCommit.put(deviceList.get(i).getAddress(), deviceList.get(i).getAddress());
        }
        deviceList.clear();
        stringHashMap.clear();
        bleDeviceAdapter.notifyDataSetChanged();
    }


    void setRv() {
        bleDeviceAdapter = new BleDeviceAdapter(this, deviceList);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mainRv.setLayoutManager(manager);
        mainRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mainRv.setAdapter(bleDeviceAdapter);

        //点击查看是不是已经
        bleDeviceAdapter.setClickCallback(new BleDeviceAdapter.ClickCallback() {
            @Override
            public void onClick(BluetoothDevice item) {
                showToast(item.getAddress());
            }
        });


        mainRefresh.setColorSchemeResources(R.color.colorAccent);
        mainRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                deviceList.clear();
                stringHashMap.clear();
                mainRefresh.setRefreshing(false);
            }
        });
    }


    void requestPermission() {
        XXPermissions.with(this)
                //蓝牙定位权限
                .permission(Permission.ACCESS_FINE_LOCATION)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            checkBle();
                            startScan();
                        } else {
                            showTipsDialog("请允许所有权限");
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MainActivity.this, permissions);
                        } else {
                            showTipsDialog("获取蓝牙定位权限失败，蓝牙无法使用");
                        }
                    }
                });
    }


    /**
     *
     */
    private void startScan() {
        BleManger.INATAN.getBluetoothAdapter().startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (device.getName() == null) {
//                    Logger.d("======name == null");
                    return;
                }
                if (stringHashMap.containsKey(device.getAddress())) {
                    Logger.d("======已在列表" + device.getName());
                    return;
                }
                if (hasCommit.containsKey(device.getAddress())) {
                    Logger.d("======已提交" + device.getName());
                    return;
                }
                stringHashMap.put(device.getAddress(), device.getAddress());
                if (bleDeviceAdapter.getData().size() < 10) {
                    bleDeviceAdapter.addData(device);
                }
//                if (isP01Dev(scanRecord)) {
//                    bleDeviceAdapter.addData(device);
//                }
            }
        });
    }


    /**
     * 根据广播包判断是不是这种锁
     */
    boolean isP01Dev(byte[] scanRecord) {
        //过滤设备类型
        ScanRecord scanRecordData = ScanRecord.parseFromBytes(scanRecord);
        //检查广播包
        if (scanRecordData.getManufacturerSpecificData() == null ||
                scanRecordData.getManufacturerSpecificData().size() == 0) {
//            Logger.d("====== 广播包null" + device.getBleName());
            return false;
        }
        //
        int key = scanRecordData.getManufacturerSpecificData().keyAt(0);
        String devType = Integer.toHexString(key).toUpperCase(Locale.US);
//        Logger.d("======devType =" + devType);
        if (!devType.startsWith("10")) {
            return false;
        }
        return true;
    }


}
