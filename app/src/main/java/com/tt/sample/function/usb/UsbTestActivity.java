package com.tt.sample.function.usb;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.tt.sample.R;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 安卓usb通信，，
 * 功能：
 * 往usb发消息
 * 接受usb返回的消息
 */
public class UsbTestActivity extends AppCompatActivity {


    TextView infoTv;
    Button button;
    UsbDomei usbDomei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb);
        infoTv = findViewById(R.id.usb_info);
        button = findViewById(R.id.usb_senddata);

        usbDomei = new UsbDomei(getApplicationContext());
        Map<String, UsbDevice> usbList = usbDomei.scanUsb();
        Set<String> keySet = usbList.keySet();
        Iterator<String> it1 = keySet.iterator();

        if (usbList.isEmpty()) {
            addInfo("没有搜索到usb设备");
        }
        while (it1.hasNext()) {
            String key = it1.next();
            addInfo(key);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy=============");
    }


    void addInfo(String str) {
        infoTv.append(str + "\n");
    }
}
