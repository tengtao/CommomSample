package com.tt.sample.net.okio;

import com.orhanobut.logger.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;


/**
 * 简单的 socket service
 */
public final class SocksServersample {
    private static final int VERSION_5 = 5;
    private static final int METHOD_NO_AUTHENTICATION_REQUIRED = 0;
    private static final int ADDRESS_TYPE_IPV4 = 1;
    private static final int ADDRESS_TYPE_DOMAIN_NAME = 3;
    private static final int COMMAND_CONNECT = 1;
    private static final int REPLY_SUCCEEDED = 0;


    OkioTestActivity okioTestActivity;
    /**
     * 无限容量的线程池
     */
    private final ExecutorService executor = Executors.newCachedThreadPool();
    //
    private ServerSocket serverSocket;
    /**
     *
     */
    private final Set<Socket> openSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());


    SocksServersample(OkioTestActivity okioTestActivity) {
        this.okioTestActivity = okioTestActivity;
    }

    /**
     * 启动服务，监听端口
     *
     * @throws IOException
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(8888);
        //
        executor.execute(this::acceptSockets);
    }

    public void shutdown() throws IOException {
        serverSocket.close();
        executor.shutdown();
    }

    public Proxy proxy() {
        return new Proxy(Proxy.Type.SOCKS,
                InetSocketAddress.createUnresolved("localhost", serverSocket.getLocalPort()));
    }

    /**
     * 接受数据
     */
    private void acceptSockets() {
        try {
            while (true) {
                Logger.d("=======开始监听端口");
                final Socket from = serverSocket.accept();
                Logger.d("=======连接建立");
                openSockets.add(from);
                //另外建一个线程用来处理数据

                executor.execute(() -> handleSocket(from));
            }
        } catch (IOException e) {
            System.out.println("shutting down: " + e);
        } finally {
            for (Socket socket : openSockets) {
                closeQuietly(socket);
            }
        }
    }


    /**
     * 处理数据
     *
     * @param fromSocket
     */
    private void handleSocket(final Socket fromSocket) {
        try {
            //用于读取输入的东西
            final Source source = Okio.buffer(Okio.source(fromSocket));
            //
            final BufferedSink fromSink = Okio.buffer(Okio.sink(fromSocket));
            Logger.d("handleSocket====== ");
            // Read the hello.
//            String data = ((BufferedSource) fromSource).readUtf8();
            Buffer buffer = new Buffer();
            String data = "";
            for (long byteCount; (byteCount = source.read(buffer, 8192L)) != -1; ) {
                // Respond to hello.
                data = data + buffer.readUtf8();
                Logger.d("getdata====== " + data);
                okioTestActivity.showServiceData(data);
                //返回数据
                //用于写出
                if (data.endsWith("end")) {
                    Sink sink = fromSink.writeUtf8("getdata====ok");
                    sink.flush();
                    Logger.d("返回数据====== ");
                }
            }
        } catch (IOException e) {
            closeQuietly(fromSocket);
            openSockets.remove(fromSocket);
            System.out.println("connect failed for " + fromSocket + ": " + e);
        }
    }


    /**
     * Read data from {@code source} and write it to {@code sink}.
     * This doesn't use {@link BufferedSink#writeAll}
     * because that method doesn't flush aggressively and we need that.
     */
    private void transfer(Socket sourceSocket, Source source, Sink sink) {
        try {
            Buffer buffer = new Buffer();
            for (long byteCount; (byteCount = source.read(buffer, 8192L)) != -1; ) {
                sink.write(buffer, byteCount);
                sink.flush();
            }
        } catch (IOException e) {
            System.out.println("transfer failed from " + sourceSocket + ": " + e);
        } finally {
            closeQuietly(sink);
            closeQuietly(source);
            closeQuietly(sourceSocket);
            openSockets.remove(sourceSocket);
        }
    }

    private void closeQuietly(Closeable c) {
        try {
            c.close();
        } catch (IOException ignored) {
        }
    }

}