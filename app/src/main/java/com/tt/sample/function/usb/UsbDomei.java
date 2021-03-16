package com.tt.sample.function.usb;


import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import java.util.Map;

/***
 * usb的逻辑
 *
 *
 *注意
 *没有设备插入的话就扫不到，
 * 插电脑的usb口也没有，
 *
 */
public class UsbDomei {

    /**
     * 传入applicatian的context
     */
    Context context;

    public UsbDomei(Context context) {
        this.context = context;
    }

    /**
     * 扫描usb串口
     */
    Map<String, UsbDevice> scanUsb() {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        Map<String, UsbDevice> usbList = usbManager.getDeviceList();
        return usbList;
    }


}
