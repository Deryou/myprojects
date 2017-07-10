package com.mr.bst.util;

import android.util.Log;

import com.mr.bst.callback.ServerCallback;
import com.mr.bst.callback.ServerConnCallback;

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
    protected Thread serverThread;
    private ServerCallback mServerCallback;
    private ServerConnCallback mServerConnCallback;
    boolean isCloseConn = false;
    private Object mObject = new Object();
    //后面多设备时得改 workThread&&mSocket
    public Thread workThread;
    private Socket mSocket;

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
        serverThread = new Thread(new Runnable() {

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
                        mSocket = mServerSocket.accept();
                        mSocket.setTcpNoDelay(true);
                        final TcpClient client = new TcpClient(mSocket);
                        client.mConnectId = new RandomGUID().toString();
                        client.mIsOpen = true;
                        Log.e(TAG, "客户端开始进行连接");
//                        synchronized (mObject) {
//                            if (!isCloseConn) {
//                                mServerCallback.ClientConnected(client);
//                            }
//                        }
                        mServerConnCallback.ClientConnected(client);
                        mThreadRun = true;
                        workThread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                while (true) {
                                    if (!mThreadRun) {
                                        break;
                                    }
                                    byte[] b = read(client);
                                    if (b != null) {
                                        synchronized (mObject) {
                                            if (isCloseConn) {
                                                Log.e(TAG, "run: 开始接收数据了");
                                                mServerCallback.OnDataReceived(client,b);
                                                Log.e(TAG, "run: 接收数据完毕");
                                            }
                                        }
                                    }

                                }

                            }
                        });
                        workThread.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        serverThread.start();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        mThreadRun = false;
        if (serverThread.isAlive()) {
            serverThread.interrupt();
        }
        if (workThread.isAlive()) {
            workThread.interrupt();
        }
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
    public boolean write(String data) {
        if (mSocket != null) {
            OutputStream outputStream;
            try {
                outputStream = mSocket.getOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(
                        outputStream);
                dataOutputStream.write(data.getBytes());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
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
        if (mServerCallback != null) {
            mServerCallback = null;
        }
        if (mSocket != null) {
            mSocket.close();
            mSocket = null;
        }
    }

    /**
     * 关闭设备连接
     * @param isClose
     */
    public void openConn(boolean isClose) {
        synchronized (mObject) {
            if (!isClose) {
                mServerCallback = null;
            }
            isCloseConn = isClose;
        }
    }

    public void setServerCallback(ServerCallback serverCallback) {
        this.mServerCallback = serverCallback;
    }

    public void setServerConnCallback(ServerConnCallback serverConnCallback) {
        this.mServerConnCallback = serverConnCallback;
    }
}
