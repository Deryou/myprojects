package com.mr.bst.gloable;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mr.bst.util.TcpServer;

/**
 * Created by MR on 2017/6/13.
 */

public class BSTApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    /**
     * 获取全局上下文
     * @return
     */
    public static Context getAppContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
