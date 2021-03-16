package com.tt.sample.net.okio;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.LinkAddress;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.logger.Logger;
import com.tt.sample.R;
import com.tt.sample.function.network.NetWorkUtils;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * @author 78tao
 */
public class OkioTestActivity extends AppCompatActivity {


    TextView infoTv;
    Button startService, sendData;
    SocksServersample socksServersample;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okio);
        infoTv = findViewById(R.id.okio_info);
        startService = findViewById(R.id.okio_startservice);
        sendData = findViewById(R.id.okio_senddata);
        showIP();
        getWifiIp();

        //
        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    socksServersample = new SocksServersample(OkioTestActivity.this);
                    socksServersample.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可以改成线程池
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendData();
                    }
                }).start();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy=============");
    }


    void showIP() {
        String netType = NetWorkUtils.getNetworkTypeName(getApplicationContext());
        String IpAddress = "";
        if (netType.equals(NetWorkUtils.NETWORK_TYPE_3G)) {
            IpAddress = NetWorkUtils.getGPRSIpAddress();
        }
        if (netType.equals(NetWorkUtils.NETWORK_TYPE_WIFI)) {
            IpAddress = NetWorkUtils.getLocalWifiIpAddress(getApplicationContext());
        }
        infoTv.setText(netType + "\n" + IpAddress);
    }

    void getWifiIp() {
        infoTv.append("\n" + getWifiRouteIPAddress(this.getApplicationContext()));
    }

    public void showServiceData(String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoTv.append("\n" + data);
            }
        });
    }

    void showClienData(String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoTv.append("\n" + data);
            }
        });
    }

    //
    void sendData() {
        //创建Socket对象
        try {
            String ip = getWifiRouteIPAddress(this.getApplicationContext());
            Logger.d("连接wifi的ip====" + ip);
            Socket socket = new Socket(ip, 8888);
            Logger.d("socket 是否连接====" + socket.isConnected());

            //用于写出
            final BufferedSink fromSink = Okio.buffer(Okio.sink(socket));
            Sink sink = fromSink.writeUtf8("data  data 消息end");
            sink.flush();
            //
            Logger.d("写出数据====");
            listenData(socket);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("=========" + e.getLocalizedMessage());
        }
    }

    void listenData(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //用于读取输入的东西
                try {
                    //用于读取输入的东西
                    final Source source = Okio.buffer(Okio.source(socket));
                    Buffer buffer = new Buffer();
                    String data = "";
                    for (long byteCount; (byteCount = source.read(buffer, 8192L)) != -1; ) {
                        data = data + buffer.readUtf8();
                        Logger.d("getdata====== " + data);
                        showClienData(data);
                        if (data.endsWith("ok")) {
                            break;
                        }
                    }
                    closeQuietly(source);
                    closeQuietly(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("=========" + e.getLocalizedMessage());
                }
            }
        }).start();
    }

    private void closeQuietly(Closeable c) {
        try {
            c.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * 获取本机IP地址
     *
     * @return
     */
    private String getlocalip() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if (ipAddress == 0) {
            return "未连接wifi";
        }
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }

    /**
     * wifi获取 已连接网络路由  路由的ip地址
     *
     * @param context
     * @return
     */
    private static String getWifiRouteIPAddress(Context context) {
        WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
        //        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
        //        System.out.println("Wifi info----->" + wifiinfo.getIpAddress());
        //        System.out.println("DHCP info gateway----->" + Formatter.formatIpAddress(dhcpInfo.gateway));
        //        System.out.println("DHCP info netmask----->" + Formatter.formatIpAddress(dhcpInfo.netmask));
        //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
        String routeIp = Formatter.formatIpAddress(dhcpInfo.gateway);
        Log.i("route ip", "wifi route ip：" + routeIp);

        return routeIp;
    }
}
