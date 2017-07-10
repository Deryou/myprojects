package com.mr.detector.util;

import android.util.Log;

import com.mr.detector.callback.ServerCallback;
import com.mr.detector.ui.activity.EquipmentActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static android.content.ContentValues.TAG;

public class TcpServer {

    public static int delayTime;
    private static TcpServer mServer;
    public boolean mThreadRun = false;
    protected ServerSocket mServerSocket;
    protected int mPort;
    private ServerCallback mServerCallback;

    /**
     * @param port 要监听的端口
     */
    public TcpServer(int port) {
        this.mPort = port;
    }

    /**
     * 获取服务单例对象
     *
     * @param port
     * @return
     */
    public static TcpServer getInstance(int port) {
        if (mServer == null) {
            synchronized (TcpServer.class) {
                if (mServer == null) {
                    mServer = new TcpServer(port);
                }
            }
        }
        return mServer;
    }

    /**
     * 开始监听，等待连接
     */
    public void connect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    if (mServerSocket != null
                            && mServerSocket.isClosed() == false) {
                        // 创建一个ServerSocket对象
                        mServerSocket.close();
                    }
                    mServerSocket = new ServerSocket(mPort);
                    mServerSocket.setReuseAddress(true);
                    mThreadRun = true;
                    while (true) {
                        if (!mThreadRun) {
                            break;
                        }
                        //调用ServerSocket的accept()方法，等待客户端连接
                        Socket socket = mServerSocket.accept();
                        socket.setTcpNoDelay(true);
                        final TcpClient client = new TcpClient(socket);
                        client.mConnectId = new RandomGUID().toString();
                        client.mIsOpen = true;
                        Log.e(TAG, "客户端开始进行连接");
                        mServerCallback.ClientConnected(client);
                        mThreadRun = true;
                        Log.e(TAG, "客户端开始连接完毕");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                while (true) {
                                    if (!mThreadRun) {
                                        break;
                                    }
                                    byte[] b = read(client);
                                    if (b != null) {
                                        Log.e(TAG, "run: 开始接收数据了");
                                        try {
                                            Thread.sleep(EquipmentActivity.TIME_GAP * 1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        mServerCallback.OnDataReceived(client,
                                                b);
                                        Log.e(TAG, "run: 接收数据完毕");
                                    }

                                }

                            }
                        }).start();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        mThreadRun = false;
        if (mServerSocket != null) {
            try {
                close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送数据
     *
     * @param data
     */
    public void write(Socket socket, byte[] data) {
        if (socket != null) {
            OutputStream outputStream;
            try {
                outputStream = socket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(
                        outputStream);
                dataOutputStream.write(data);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public byte[] read(TcpClient client) {
        try {
            Socket socket = client.mSocket;
            InputStream inputstream = socket.getInputStream();
            int avaliable = inputstream.available();
            if (avaliable > 0) {
                byte[] readData = new byte[avaliable];
                avaliable = inputstream.read(readData, 0, avaliable);
                return readData;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 关闭资源
     *
     * @throws IOException
     */
    protected void close() throws IOException {
        if (mServerSocket != null) {
            mServerSocket.close();
            mServerSocket = null;
        }

        mServerCallback = null;
    }

    public void setServerCallback(ServerCallback serverCallback) {
        this.mServerCallback = serverCallback;
    }

}
