package com.tt.sample.net.udp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tt.sample.R;


/**
 * 安卓udp简单发送接收
 * 功能：
 * 发udp消息
 * 接受udp消息
 */
public class UdpTestActivity extends AppCompatActivity {


    TextView infoTv;
    Button button;

    UdpDomei udpDomei;
    int testNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_test);
        infoTv = findViewById(R.id.udp_info);
        button = findViewById(R.id.udp_senddata);
        udpDomei = new UdpDomei(this.getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    udpDomei.sendudpMess(InetAddress.getByName("192.168.43.255"), 3333, "客户端的消息===" + (testNum));
//
//
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
                udpDomei.sendUdpBroadcast(3333, "客户端的消息===" + (testNum));
                testNum++;
            }
        });

        udpDomei.startUdpListen(new UdpDomei.UdpMsgListen() {
            @Override
            public void callback(String msg) {
                System.out.println("onDestroy=============" + msg);
            }
        });
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
