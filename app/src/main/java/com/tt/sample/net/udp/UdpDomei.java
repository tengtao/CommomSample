package com.tt.sample.net.udp;


import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.Inflater;

/***
 *udp收发
 *
 *
 *
 */
public class UdpDomei {

    Context context;
    boolean isListenUdp = true;

    public UdpDomei(Context context) {
        this.context = context;
    }

    /**
     * 发送udp消息
     * 因为安卓不给在主线程发网络请求，所以要新建线程
     * 如果使用很频繁建议使用线程池，，
     * <p>
     * ap隔离：
     * wifi开启AP隔离后局域网内的ip就不能通信，
     * 手机的热点默认开启AP隔离，
     * 比如热点是192.163.43.1，，两台其他手机连接了这个热点，ip分别是A（192.163.43.5），b（192.163.43.6）
     * a和b之间不能通过udp发消息，发送但是接收不到，但是192.163.43.1可以收到，
     */
    void sendudpMess(InetAddress inetAddress, int prot, String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte data[] = msg.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, prot);
                    DatagramSocket socket = new DatagramSocket();
                    socket.send(packet);
                    //如果不接收消息，，那么就关掉
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /***
     * 发送udp局域网广播
     *
     * 如果本机链接的wiif地址是192.168.43.1，那么把udp包发送到192.168.43.255就可以，，
     * 我也不知道是什么原理
     *
     * 有的路由器是禁止发送广播的，那只能采用扫描的方式了
     */
    void sendUdpBroadcast(int prot, String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket socket = null;
                try {
                    InetAddress inetAddress = getbcip(context);
                    byte[] data = msg.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket packet = new DatagramPacket(data, data.length, inetAddress, prot);
                    socket = new DatagramSocket();
                    socket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        }).start();
    }


    /**
     * 获取当前IP地址的广播地址
     * https://blog.csdn.net/kingroc/article/details/52185592
     */

    InetAddress getbcip(Context context) throws UnknownHostException {
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        //这里获取了IP地址，获取到的IP地址还是int类型的。
        int ip = wifiInfo.getIpAddress();
        //这一步就是将本机的IP地址转换成xxx.xxx.xxx.255
        int broadCastIP = ip | 0xFF000000;
        Logger.d("=======" + Formatter.formatIpAddress(broadCastIP));
        return InetAddress.getByName(Formatter.formatIpAddress(broadCastIP));
    }

    /***
     * 监听udp端口
     *
     *需要在子线程监听
     *
     */
    void startUdpListen(UdpMsgListen udpMsgListen) {
        int port = 3333;

        new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramSocket server = null;
                try {
                    server = new DatagramSocket(port);
                    byte[] buffer = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    while (isListenUdp) {
                        try {
                            server.receive(packet);
                            String s = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
                            System.out.println("address : " + packet.getAddress()
                                    + ", port : " + packet.getPort()
                                    + ", content : " + s);
                            udpMsgListen.callback(s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } finally {
                    if (server != null) {
                        server.close();
                    }
                }
            }
        }).start();
    }

    void stopUdpListen() {
        isListenUdp = false;
    }


    /**
     * 回调接口，
     */
    public interface UdpMsgListen {
        void callback(String msg);
    }

}
