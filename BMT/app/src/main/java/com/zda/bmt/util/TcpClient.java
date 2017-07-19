package com.zda.bmt.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by MR on 2017/4/27.
 */

public class TcpClient {

    //连接ID
    public String mConnectId;
    //打开状态
    public boolean mIsOpen;
    //线程运行状态
    protected boolean mThreadIsRun;
    //远程端口
    public int mRemotePort;
    //远程IP
    public String mRemoteIp;
    //本地端口
    public int mLocalPort;
    //本地IP
    public String mLocalIp;
    public Socket mSocket = null;

    public byte[] mReadData;
    public int mAvailabe = 0;
    public int mReadLength = 4096;
    public InputStream mInputStream = null;
    public OutputStream mOutputStream = null;
    public DataOutputStream mDataOutputStream = null;

    private ConnectCallback mConnectCallback;

    public TcpClient(String ip, int port) {
        mRemoteIp = ip;
        mRemotePort = port;
        mReadData = new byte[mReadLength];

        try {
            mSocket = new Socket();
            mSocket.setTcpNoDelay(false);
            mSocket.setSoLinger(true,0);
            mSocket.setKeepAlive(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TcpClient(Socket socket) {
        mReadData = new byte[mReadLength];
        mSocket = socket;

        mRemotePort = mSocket.getPort();
        mLocalPort = mSocket.getLocalPort();
        mLocalIp = mSocket.getLocalAddress().getHostAddress();
        mRemoteIp = mSocket.getInetAddress().getHostAddress();
    }

    /**
     * 开始连接
     */
    public void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress inetAddress = InetAddress.getByName(mRemoteIp);
                    mSocket.connect(new InetSocketAddress(inetAddress,mRemotePort),3000);
                    mSocket.setSendBufferSize(3000);
                    mSocket.setReceiveBufferSize(3000);
                    mIsOpen = true;
                    mConnectCallback.onConnectSuccess();
                    mThreadIsRun = true;
                    new ReceiveDataThread().start();
                } catch (Exception e) {
                    if (mConnectCallback != null) {
                        mConnectCallback.onConnectFail("连接失败");
                    }
                    if (mSocket != null) {
                        try {
                            mSocket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 关闭连接
     */
    public void disconnect() {
        try {
            mThreadIsRun = false;
            mInputStream = null;
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受数据线程
     */
    protected class ReceiveDataThread extends Thread {
        public ReceiveDataThread() {
            this.setPriority(Thread.MAX_PRIORITY);
        }

        @Override
        public void run() {
            try {
                if (mSocket != null) {
                    mInputStream = mSocket.getInputStream();
                }
                while (mThreadIsRun) {
                    Thread.sleep(50);
                    if (mInputStream != null) {
                        mAvailabe = mInputStream.available();
                        if (mAvailabe > 0) {
                            if (mAvailabe > mReadLength) {
                                mAvailabe = mReadLength;
                            }
                            mAvailabe = mInputStream.read(mReadData,0,mAvailabe);
                            mConnectCallback.onDataReceived(mReadData,mAvailabe);
                        }
                    }
                }
                close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean write(byte[] data) {
        if (mSocket != null) {
            try {
                mOutputStream = mSocket.getOutputStream();
                mDataOutputStream = new DataOutputStream(mOutputStream);
                mDataOutputStream.write(data);
                return true;
            } catch (IOException e) {
                mConnectCallback.onConnectFail("写入配置失败");
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 关闭资源
     * @throws IOException
     */
    public void close() throws IOException {
        if (mInputStream != null) {
            mInputStream.close();
            mInputStream = null;
        }
        if (mOutputStream != null) {
            mOutputStream.close();
            mOutputStream = null;
        }
        if (mDataOutputStream != null) {
            mDataOutputStream.close();
            mDataOutputStream = null;
        }
        if (mSocket != null) {
            mSocket.close();
            mSocket = null;
        }
        mThreadIsRun = false;
        mConnectCallback = null;
    }

    public interface ConnectCallback {
        //连接成功
        void onConnectSuccess();

        //连接失败
        void onConnectFail(String data);

        //收到数据
        void onDataReceived(byte[] data, int availabe);
    }

    public void setConnectCallback(ConnectCallback connectCallback) {
        mConnectCallback = connectCallback;
    }
}
