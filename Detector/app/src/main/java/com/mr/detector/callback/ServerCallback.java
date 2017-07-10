package com.mr.detector.callback;

import com.mr.detector.util.TcpClient;

/**
 * TCP服务端毁回调
 */
public interface ServerCallback {
    /**
     * 新客户端连接成功
     *
     * @param client
     */
    void ClientConnected(TcpClient client);

    /**
     * 接收到客户端数据
     *
     * @param client
     * @param data
     */
    void OnDataReceived(TcpClient client, byte[] data);
}
