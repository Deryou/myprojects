package com.zda.bmt.callback;

import com.zda.bmt.util.TcpClient;

/**
 * Created by MR on 2017/6/23.
 */

public interface ServerConnCallback {
    /**
     * 新客户端连接成功
     *
     * @param client
     */
    void ClientConnected(TcpClient client);
}
