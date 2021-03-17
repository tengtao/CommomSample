package com.tt.sample.function.ble.ui;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.orhanobut.logger.Logger;
import com.tt.sample.MyApplication;
import com.tt.sample.R;
import com.tt.sample.base.BaseActivity;
import com.tt.sample.function.ble.BleConnectCallBack;
import com.tt.sample.function.ble.BleManger;
import com.tt.sample.function.ble.HexUtils;
import com.tt.sample.function.ble.work.SendManger;
import com.tt.sample.function.permission.PermissionActivity;
import com.tt.sample.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * https://blog.csdn.net/laoguanhua/article/details/81385270
 */
public class BleSendMsgActivity extends BaseActivity {


    @BindView(R.id.main_dev_name)
    TextView mainDevName;
    @BindView(R.id.main_dev_mac)
    TextView mainDevMac;
    @BindView(R.id.main_connect_state)
    TextView mainConnectState;
    @BindView(R.id.main_log_rv)
    RecyclerView mainLogRv;
    @BindView(R.id.main_clean_back)
    Button mainCleanBack;
    @BindView(R.id.main_ed)
    EditText mainEd;
    @BindView(R.id.main_send)
    Button mainSend;


    //
    BleConnectCallBack bleConnectCallBack;
    List<String> stringList = new ArrayList<>();

    BleBackMsgAdapter BleBackMsgAdapter;

    private MutableLiveData<String> mLiveData;

    String devMac = "DE:5F:50:E6:FE:D0";

    @Override
    public int getLayoutResID() {
        return R.layout.activity_ble_sendmsg;
    }

    @Override
    public void inti() {
        super.inti();
        requestPermission();
        setBle();

        mainDevName.setText(devMac);
        mainDevMac.setText(devMac);
        if (BleManger.INATAN.isConnect()) {
            mainConnectState.setText("连接");
        } else {
            mainConnectState.setText("断开");
        }
        setRv();


        //liveData基本使用
        mLiveData = new MutableLiveData<>();
        mLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
    }

    /**
     * 定位权限是蓝牙ble扫描需要
     */
    private void requestPermission() {
        String[] permission = new String[]{Permission.ACCESS_FINE_LOCATION};


        XXPermissions.with(this)
                //蓝牙定位权限
                .permission(Permission.ACCESS_FINE_LOCATION)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            //全部允许
                            //发送东西
                            byte[] data = HexUtils.hexStr2Bytes("");
                            BleManger.INATAN.postData(devMac, data);
                        } else {
                            //还有部分权限没允许
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(BleSendMsgActivity.this, permissions);
                        } else {
                            //提示
                        }
                    }
                });
        Logger.d("==========请求权限");
    }

    //点击监听
    @OnClick({R.id.main_clean_back, R.id.main_send, R.id.main_connect_state})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.main_clean_back:
                cleanLog();
                break;
            case R.id.main_send:
//                sendMsg();
//                A1LockHandler.setMac(MyApplication.devMac);
//                A1LockHandler.addLock();
                byte[] data = HexUtils.hexStr2Bytes("");
                BleManger.INATAN.postData(devMac, data);
                break;
            case R.id.main_connect_state:
//                connectDev();
                BleManger.INATAN.disConnectByCode();
//                break;
            default:
                break;
        }
    }

    private void connectDev() {
        BleManger.INATAN.connectDevice(devMac, bleConnectCallBack);
    }


    void setRv() {
        BleBackMsgAdapter = new BleBackMsgAdapter(this, stringList);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mainLogRv.setLayoutManager(manager);
        //要和<item name="android:listDivider">@android:color/black</item> 一起使用
        mainLogRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mainLogRv.setAdapter(BleBackMsgAdapter);
    }


    void sendMsg() {
        String str = mainEd.getText().toString().trim();
        byte[] data = HexUtils.hexStr2Bytes(str);
        //16进制
        String cmd = "ABCDABCD";
        data = HexUtils.hexStr2Bytes(cmd);
        BleManger.INATAN.sendData(data);
    }

    void cleanLog() {
        stringList.clear();
        BleBackMsgAdapter.notifyDataSetChanged();
    }

    void addLog(String log) {
        stringList.add(log);
        Logger.d("=====" + stringList);
        BleBackMsgAdapter.setNewData(stringList);
    }


    void setBle() {
        bleConnectCallBack = new BleConnectCallBack("main") {
            @Override
            public void connectSuccess() {
                super.connectSuccess();
                mainConnectState.setText("连接");
            }

            @Override
            public void connectFail(String errorMsg) {
                mainConnectState.setText(errorMsg);
                //
                mainSend.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        byte[] data = HexUtils.hexStr2Bytes("");
                        BleManger.INATAN.postData(devMac, data);
                    }
                }, 4000);
            }

            @Override
            public void writeTimeOut() {
                addLog("发送超时");
            }

            @Override
            public void handleMsg(String hexString, byte[] bytes) {
                addLog(hexString);
                BleManger.INATAN.disConnectByCode();
            }

            @Override
            public void sendFail(String errorCode) {
                addLog(errorCode);
            }
        };
        BleManger.INATAN.addBleConnectCallBack(bleConnectCallBack);
    }


    public void addLock() {
        byte[] data = HexUtils.hexStr2Bytes("ABCD");
        SendManger.INATAN.inti(devMac)
                .listMsg("AddMPermisson", new SendManger.MsgCallBack() {
                    @Override
                    public void callback(String work, byte[] data, String hexString) {
                        if (hexString.startsWith("f10301")) {
                            String backString = hexString.substring(6, 8);
                            if ("00".equals(backString)) {
                                byte[] getPwdList = HexUtils.hexStr2Bytes("");
                                SendManger.INATAN.sendListData("setkey", getPwdList);
                            } else {
                                Logger.d("=======AddMPermisson 失败");
                            }
                        }
                    }
                })
                //
                .listMsg("setkey", new SendManger.MsgCallBack() {
                    @Override
                    public void callback(String work, byte[] data, String hexString) {
                        if (hexString.startsWith("f12401")) {
                            String backString = hexString.substring(6, 8);
                            if ("00".equals(backString)) {
                                byte[] getPwdList = HexUtils.hexStr2Bytes("");
                                SendManger.INATAN.sendListData("updateTime", getPwdList);
                            } else {
                                Logger.d("=======setKey 失败");
                            }
                        }
                    }
                })
                .listMsg("updateTime", new SendManger.MsgCallBack() {
                    @Override
                    public void callback(String work, byte[] data, String hexString) {
                        if (hexString.startsWith("f10f01")) {
                            String backString = hexString.substring(6, 8);
                            if ("00".equals(backString)) {
                                Logger.d("=====同步时间成功");
                            } else {
                                Logger.d("=====同步时间失败");
                            }
                        }
                    }
                })
                //最后发送
                .startSend("AddMPermisson", data);
    }


}
