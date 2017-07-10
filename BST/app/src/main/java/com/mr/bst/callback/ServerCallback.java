package com.mr.bst.callback;


import com.mr.bst.util.TcpClient;

/**
 * TCP服务端毁回调
 */
public interface ServerCallback {

    /**
     * 接收到客户端数据
     *
     * @param client
     * @param data
     */
    void OnDataReceived(TcpClient client, byte[] data);
}
